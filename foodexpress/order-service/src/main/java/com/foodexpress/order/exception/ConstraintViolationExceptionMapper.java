package com.foodexpress.order.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        ApiError err = new ApiError();
        err.status = 400;
        err.error = "Validation Error";
        err.message = "La requête contient des erreurs de validation";
        err.path = uriInfo.getPath();
        err.service = "order-service";
        err.violations = e.getConstraintViolations().stream()
                .map(v -> new ApiError.Violation(v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());

        return Response.status(400).entity(err).build();
    }
}
