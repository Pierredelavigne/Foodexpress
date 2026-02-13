package com.foodexpress.customer.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(DuplicateResourceException e) {
        ApiError err = new ApiError();
        err.status = 409;
        err.error = "Conflict";
        err.message = e.getMessage();
        err.path = uriInfo.getPath();
        err.service = "customer-service";
        return Response.status(409).entity(err).build();
    }
}
