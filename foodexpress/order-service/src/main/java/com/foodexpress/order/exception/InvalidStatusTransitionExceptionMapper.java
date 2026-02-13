package com.foodexpress.order.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidStatusTransitionExceptionMapper implements ExceptionMapper<InvalidStatusTransitionException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(InvalidStatusTransitionException e) {
        ApiError err = new ApiError();
        err.status = 422;
        err.error = "Invalid Status Transition";
        err.message = e.getMessage();
        err.path = uriInfo.getPath();
        err.service = "order-service";
        return Response.status(422).entity(err).build();
    }
}
