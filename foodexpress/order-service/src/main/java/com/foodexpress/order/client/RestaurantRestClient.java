package com.foodexpress.order.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/restaurants")
@RegisterRestClient(configKey = "restaurant-api")
public interface RestaurantRestClient {

    @GET
    @Path("/{id}")
    RestaurantResponse get(@PathParam("id") Long id);

    @GET
    @Path("/{id}/dishes/{dishId}/exists")
    boolean dishExists(@PathParam("id") Long restaurantId, @PathParam("dishId") Long dishId);

    class RestaurantResponse {
        public RestaurantDto restaurant;
        public String menu;
    }

    class RestaurantDto {
        public Long id;
        public String name;
        public String cuisine;
        public String address;
        public String city;
        public String phone;
        public Double rating;
        public boolean active;
    }
}
