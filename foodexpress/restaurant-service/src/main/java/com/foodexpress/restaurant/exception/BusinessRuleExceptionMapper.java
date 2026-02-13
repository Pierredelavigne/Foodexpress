package com.foodexpress.restaurant.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessRuleExceptionMapper implements ExceptionMapper<BusinessRuleException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(BusinessRuleException e) {
        ApiError err = new ApiError();
        err.status = 422;
        err.error = "Unprocessable Entity";
        err.message = e.getMessage();
        err.path = uriInfo.getPath();
        err.service = "restaurant-service";
        return Response.status(422).entity(err).build();
    }
}
