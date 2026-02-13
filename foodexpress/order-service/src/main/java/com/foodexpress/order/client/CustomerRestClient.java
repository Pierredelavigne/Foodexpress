package com.foodexpress.order.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/customers")
@RegisterRestClient(configKey = "customer-api")
public interface CustomerRestClient {

    @GET
    @Path("/{id}/exists")
    boolean exists(@PathParam("id") Long id);
}
