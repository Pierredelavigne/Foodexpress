package com.foodexpress.order.resource;

import com.foodexpress.order.dto.*;
import com.foodexpress.order.entity.Order;
import com.foodexpress.order.service.OrderService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService service;

    @POST
    public OrderResponse create(@Valid OrderCreateRequest req) {
        return service.create(req);
    }

    @GET
    public PagedResponse<Order> list(@QueryParam("page") @DefaultValue("0") int page,
                                    @QueryParam("size") @DefaultValue("10") int size) {
        return service.listAll(page, size);
    }

    @GET
    @Path("/{id}")
    public Order get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @PUT
    @Path("/{id}/status")
    public Order updateStatus(@PathParam("id") Long id, @Valid OrderStatusUpdateRequest req) {
        return service.updateStatus(id, req.status);
    }

    @DELETE
    @Path("/{id}")
    public void cancel(@PathParam("id") Long id) {
        service.cancel(id);
    }

    @GET
    @Path("/{id}/full")
    public OrderFullResponse full(@PathParam("id") Long id) {
        return service.full(id);
    }
}
