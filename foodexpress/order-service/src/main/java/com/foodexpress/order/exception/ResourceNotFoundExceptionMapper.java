package com.foodexpress.order.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ResourceNotFoundException e) {
        ApiError err = new ApiError();
        err.status = 404;
        err.error = "Not Found";
        err.message = e.getMessage();
        err.path = uriInfo.getPath();
        err.service = "order-service";
        return Response.status(404).entity(err).build();
    }
}
