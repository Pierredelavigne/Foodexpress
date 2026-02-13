package com.foodexpress.order.service;

import com.foodexpress.order.client.CustomerRestClient;
import com.foodexpress.order.client.DeliveryRestClient;
import com.foodexpress.order.client.RestaurantRestClient;
import com.foodexpress.order.config.OrderConfig;
import com.foodexpress.order.dto.*;
import com.foodexpress.order.entity.*;
import com.foodexpress.order.exception.*;
import com.foodexpress.order.repository.OrderRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepo;

    @Inject
    OrderConfig config;

    @Inject @RestClient
    CustomerRestClient customerClient;

    @Inject @RestClient
    RestaurantRestClient restaurantClient;

    @Inject @RestClient
    DeliveryRestClient deliveryClient;

    private static int clampSize(int size) { return Math.max(1, Math.min(size, 50)); }
    private static int clampPage(int page) { return Math.max(0, page); }

    public PagedResponse<Order> listAll(int page, int size) {
        page = clampPage(page);
        size = clampSize(size);

        PanacheQuery<Order> q = orderRepo.findAll();
        long total = q.count();
        List<Order> data = q.page(Page.of(page, size)).list();

        return new PagedResponse<>(data, page, size, total);
    }

    public Order get(Long id) {
        Order o = orderRepo.findById(id);
        if (o == null) throw new ResourceNotFoundException("Order with id " + id + " not found");
        return o;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest req) {

        // 1) customer exists
        try {
            if (!customerClient.exists(req.customerId)) {
                throw new ResourceNotFoundException("Customer " + req.customerId + " not found");
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceUnavailableException("customer-service unavailable");
        }

        // 2) restaurant exists (and pickup address)
        RestaurantRestClient.RestaurantResponse r;
        try {
            r = restaurantClient.get(req.restaurantId);
            if (r == null || r.restaurant == null) {
                throw new ResourceNotFoundException("Restaurant " + req.restaurantId + " not found");
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceUnavailableException("restaurant-service unavailable");
        }

        // 3) verify dishes + compute total (simple pricing for TP)
        Order order = new Order();
        order.customerId = req.customerId;
        order.restaurantId = req.restaurantId;
        order.deliveryAddress = req.deliveryAddress;
        order.status = OrderStatus.CREATED;
        order.createdAt = LocalDateTime.now();
        order.updatedAt = LocalDateTime.now();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest it : req.items) {
            try {
                if (!restaurantClient.dishExists(req.restaurantId, it.dishId)) {
                    throw new ResourceNotFoundException("Dish " + it.dishId + " not found for restaurant " + req.restaurantId);
                }
            } catch (ResourceNotFoundException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceUnavailableException("restaurant-service unavailable (dish exists)");
            }

            // NOTE TP: pour simplifier on met un prix fixe.
            // Si tu veux, on améliorera en ajoutant un endpoint dish details côté restaurant.
            BigDecimal unitPrice = new BigDecimal("10.00");

            OrderItem item = new OrderItem();
            item.dishId = it.dishId;
            item.quantity = it.quantity;
            item.unitPrice = unitPrice;
            item.dishName = "Dish#" + it.dishId;
            item.order = order;

            order.items.add(item);
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(it.quantity)));
        }

        order.totalAmount = total;

        // 4) business rule min amount
        if (order.totalAmount.compareTo(config.minAmount) < 0) {
            throw new BusinessRuleException("Minimum order amount is " + config.minAmount);
        }

        // 5) persist
        orderRepo.persist(order);

        // 6) call delivery-service
        DeliveryDto delivery = null;
        try {
            DeliveryRestClient.DeliveryCreateRequest dreq = new DeliveryRestClient.DeliveryCreateRequest();
            dreq.orderId = order.id;
            dreq.pickupAddress = r.restaurant.address;
            dreq.deliveryAddress = order.deliveryAddress;

            DeliveryRestClient.DeliveryDto d = deliveryClient.create(dreq);
            if (d != null) {
                delivery = new DeliveryDto();
                delivery.id = d.id;
                delivery.orderId = d.orderId;
                delivery.driverId = d.driverId;
                delivery.status = d.status;
                delivery.pickupAddress = d.pickupAddress;
                delivery.deliveryAddress = d.deliveryAddress;
                delivery.estimatedTime = d.estimatedTime;
            }
        } catch (Exception e) {
            throw new ServiceUnavailableException("delivery-service unavailable");
        }

        return new OrderResponse(order, delivery);
    }

    @Transactional
    public Order updateStatus(Long id, String statusStr) {
        Order o = get(id);

        OrderStatus target;
        try {
            target = OrderStatus.valueOf(statusStr);
        } catch (Exception e) {
            throw new BusinessRuleException("Unknown status: " + statusStr);
        }

        if (!isValidTransition(o.status, target)) {
            throw new InvalidStatusTransitionException("Invalid transition " + o.status + " -> " + target);
        }

        o.status = target;
        o.updatedAt = LocalDateTime.now();
        return o;
    }

    @Transactional
    public void cancel(Long id) {
        Order o = get(id);
        if (o.status == OrderStatus.PICKED_UP || o.status == OrderStatus.DELIVERED) {
            throw new BusinessRuleException("Cannot cancel an order after PICKED_UP");
        }
        o.status = OrderStatus.CANCELLED;
        o.updatedAt = LocalDateTime.now();
    }

    public OrderFullResponse full(Long id) {
        Order o = get(id);

        // on fait simple : full = order + restaurant basic + customer basic (delivery null tant que delivery GET pas fait)
        CustomerDto customer = new CustomerDto();
        RestaurantDto restaurant = new RestaurantDto();

        try {
            // exists only in client currently; si tu veux le GET customer, je te l'ajoute après
            customer.id = o.customerId;
        } catch (Exception e) {
            throw new ServiceUnavailableException("customer-service unavailable (full)");
        }

        try {
            RestaurantRestClient.RestaurantResponse r = restaurantClient.get(o.restaurantId);
            if (r != null && r.restaurant != null) {
                restaurant.id = r.restaurant.id;
                restaurant.name = r.restaurant.name;
                restaurant.cuisine = r.restaurant.cuisine;
                restaurant.address = r.restaurant.address;
                restaurant.city = r.restaurant.city;
                restaurant.phone = r.restaurant.phone;
                restaurant.rating = r.restaurant.rating;
                restaurant.active = r.restaurant.active;
            }
        } catch (Exception e) {
            throw new ServiceUnavailableException("restaurant-service unavailable (full)");
        }

        return new OrderFullResponse(o, customer, restaurant, null);
    }

    private boolean isValidTransition(OrderStatus from, OrderStatus to) {
        if (from == to) return true;
        return switch (from) {
            case CREATED -> (to == OrderStatus.CONFIRMED || to == OrderStatus.CANCELLED);
            case CONFIRMED -> (to == OrderStatus.PREPARING || to == OrderStatus.CANCELLED);
            case PREPARING -> (to == OrderStatus.READY || to == OrderStatus.CANCELLED);
            case READY -> (to == OrderStatus.PICKED_UP || to == OrderStatus.CANCELLED);
            case PICKED_UP -> (to == OrderStatus.DELIVERED);
            case DELIVERED, CANCELLED -> false;
        };
    }
}
