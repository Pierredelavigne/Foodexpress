package com.foodexpress.restaurant.resource;

import com.foodexpress.restaurant.dto.DishRequest;
import com.foodexpress.restaurant.dto.PagedResponse;
import com.foodexpress.restaurant.dto.RestaurantRequest;
import com.foodexpress.restaurant.dto.RestaurantResponse;
import com.foodexpress.restaurant.entity.Dish;
import com.foodexpress.restaurant.entity.Restaurant;
import com.foodexpress.restaurant.service.RestaurantService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantResource {

    @Inject
    RestaurantService service;

    @POST
    public Restaurant create(@Valid RestaurantRequest req) {
        return service.create(req);
    }

    // Listing + filtres + pagination
    // GET /api/restaurants?page=0&size=10&cuisine=Italian&city=Lille&minRating=4.2&open=true
    @GET
    public PagedResponse<Restaurant> list(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("cuisine") String cuisine,
            @QueryParam("city") String city,
            @QueryParam("rating") Double minRating,
            @QueryParam("open") Boolean open
    ) {
        return service.list(page, size, cuisine, city, minRating, open);
    }

    @GET
    @Path("/{id}")
    public RestaurantResponse get(
        @PathParam("id") 
        Long id,
        @QueryParam("format")  
        @DefaultValue("detailed") String format) {

        Restaurant r = service.get(id);
        String menu = service.formatMenu(id, format);
        return new RestaurantResponse(r, menu);
    }


    @PUT
    @Path("/{id}")
    public Restaurant update(@PathParam("id") Long id, @Valid RestaurantRequest req) {
        return service.update(id, req);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }

    // Add dish
    @POST
    @Path("/{id}/dishes")
    public Dish addDish(@PathParam("id") Long restaurantId, @Valid DishRequest req) {
        return service.addDish(restaurantId, req);
    }

    // Remove dish
    @DELETE
    @Path("/{id}/dishes/{dishId}")
    public void removeDish(@PathParam("id") Long restaurantId, @PathParam("dishId") Long dishId) {
        service.removeDish(restaurantId, dishId);
    }

    // Dish exists
    @GET
    @Path("/{id}/dishes/{dishId}/exists")
    public boolean dishExists(@PathParam("id") Long restaurantId, @PathParam("dishId") Long dishId) {
        return service.dishExists(restaurantId, dishId);
    }

    // Search cuisine
    @GET
    @Path("/search")
    public PagedResponse<Restaurant> searchCuisine(
            @QueryParam("cuisine") String cuisine,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {
        if (cuisine == null || cuisine.isBlank()) throw new BadRequestException("cuisine query param is required");
        return service.list(page, size, cuisine, null, null, null);
    }
}
