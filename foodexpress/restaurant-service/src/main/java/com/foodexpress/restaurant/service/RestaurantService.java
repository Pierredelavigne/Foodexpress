package com.foodexpress.restaurant.service;

import com.foodexpress.restaurant.dto.DishRequest;
import com.foodexpress.restaurant.dto.PagedResponse;
import com.foodexpress.restaurant.dto.RestaurantRequest;
import com.foodexpress.restaurant.entity.Dish;
import com.foodexpress.restaurant.entity.Restaurant;
import com.foodexpress.restaurant.exception.ResourceNotFoundException;
import com.foodexpress.restaurant.formatter.MenuFormatter;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class RestaurantService {

    @Inject
    @Named("simple")
    MenuFormatter simpleFormatter;

    @Inject
    @Named("detailed")
    MenuFormatter detailedFormatter;


    private static int clampSize(int size) { return Math.max(1, Math.min(size, 50)); }
    private static int clampPage(int page) { return Math.max(0, page); }

    private PagedResponse<Restaurant> toPaged(PanacheQuery<Restaurant> q, int page, int size) {
        page = clampPage(page);
        size = clampSize(size);
        long total = q.count();
        List<Restaurant> data = q.page(Page.of(page, size)).list();
        return new PagedResponse<>(data, page, size, total);
    }

    public PagedResponse<Restaurant> list(int page, int size, String cuisine, String city, Double minRating, Boolean open) {
        StringBuilder jpql = new StringBuilder("active = true");
        new Object();
        var params = new java.util.HashMap<String, Object>();

        if (cuisine != null && !cuisine.isBlank()) {
            jpql.append(" and cuisine = :cuisine");
            params.put("cuisine", cuisine);
        }
        if (city != null && !city.isBlank()) {
            jpql.append(" and city = :city");
            params.put("city", city);
        }
        if (minRating != null) {
            jpql.append(" and rating >= :minRating");
            params.put("minRating", minRating);
        }
        if (open != null && open) {
            LocalTime now = LocalTime.now();
            jpql.append(" and openingTime <= :now and closingTime >= :now");
            params.put("now", now);
        }

        PanacheQuery<Restaurant> q = Restaurant.find(jpql.toString(), params);
        return toPaged(q, page, size);
    }

    public Restaurant get(Long id) {
        Restaurant r = Restaurant.findById(id);
        if (r == null) throw new ResourceNotFoundException("Restaurant with id " + id + " not found");
        return r;
    }

    @Transactional
    public Restaurant create(RestaurantRequest req) {
        Restaurant r = new Restaurant();
        r.name = req.name;
        r.cuisine = req.cuisine;
        r.address = req.address;
        r.city = req.city;
        r.phone = req.phone;
        r.rating = req.rating;
        r.openingTime = LocalTime.parse(req.openingTime);
        r.closingTime = LocalTime.parse(req.closingTime);
        r.active = req.active == null ? true : req.active;

        r.persist();
        return r;
    }

    @Transactional
    public Restaurant update(Long id, RestaurantRequest req) {
        Restaurant r = get(id);
        r.name = req.name;
        r.cuisine = req.cuisine;
        r.address = req.address;
        r.city = req.city;
        r.phone = req.phone;
        r.rating = req.rating;
        r.openingTime = LocalTime.parse(req.openingTime);
        r.closingTime = LocalTime.parse(req.closingTime);
        if (req.active != null) r.active = req.active;
        return r;
    }

    @Transactional
    public void delete(Long id) {
        Restaurant r = get(id);
        r.delete();
    }

    @Transactional
    public Dish addDish(Long restaurantId, DishRequest req) {
        Restaurant r = get(restaurantId);

        Dish d = new Dish();
        d.name = req.name;
        d.description = req.description;
        d.price = req.price;
        d.category = req.category;
        d.available = req.available == null ? true : req.available;
        d.allergens = req.allergens;
        d.restaurant = r;

        r.dishes.add(d);
        d.persist();
        return d;
    }

    @Transactional
    public void removeDish(Long restaurantId, Long dishId) {
        Restaurant r = get(restaurantId);
        Dish d = Dish.findById(dishId);
        if (d == null || d.restaurant == null || !d.restaurant.id.equals(r.id)) {
            throw new ResourceNotFoundException("Dish " + dishId + " not found for restaurant " + restaurantId);
        }
        r.dishes.remove(d);
        d.delete();
    }

    public boolean dishExists(Long restaurantId, Long dishId) {
        Dish d = Dish.findById(dishId);
        return d != null && d.restaurant != null && d.restaurant.id.equals(restaurantId);
    }

    public String formatMenu(Long restaurantId, String format) {
        Restaurant r = get(restaurantId);

        // force le chargement des dishes pendant qu'on est encore dans le contexte
        r.dishes.size();

        if ("simple".equalsIgnoreCase(format)) {
            return simpleFormatter.format(r);
        }
        return detailedFormatter.format(r);
    }
}
