package com.foodexpress.order.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ServiceUnavailableExceptionMapper implements ExceptionMapper<ServiceUnavailableException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ServiceUnavailableException e) {
        ApiError err = new ApiError();
        err.status = 503;
        err.error = "Service Unavailable";
        err.message = e.getMessage();
        err.path = uriInfo.getPath();
        err.service = "order-service";
        return Response.status(503).entity(err).build();
    }
}
