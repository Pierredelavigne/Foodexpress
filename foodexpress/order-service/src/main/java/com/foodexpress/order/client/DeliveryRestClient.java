package com.foodexpress.order.client;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/deliveries")
@RegisterRestClient(configKey = "delivery-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeliveryRestClient {

    @POST
    DeliveryDto create(DeliveryCreateRequest req);

    class DeliveryCreateRequest {
        public Long orderId;
        public String pickupAddress;
        public String deliveryAddress;
    }

    class DeliveryDto {
        public Long id;
        public Long orderId;
        public Long driverId;
        public String status;
        public String pickupAddress;
        public String deliveryAddress;
        public String estimatedTime;
    }
}
