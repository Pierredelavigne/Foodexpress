package com.foodexpress.customer.resource;

import com.foodexpress.customer.dto.CustomerRequest;
import com.foodexpress.customer.dto.PagedResponse;
import com.foodexpress.customer.entity.Customer;
import com.foodexpress.customer.exception.ResourceNotFoundException;
import com.foodexpress.customer.service.CustomerService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CustomerService service;

    // =========================
    // CREATE
    // =========================
    @POST
    public Customer create(@Valid CustomerRequest req) {
        return service.create(req);
    }

    // =========================
    // LIST (paginated)
    // =========================
    @GET
    public PagedResponse<Customer> listAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        return service.listAllPaged(page, size);
    }

    // =========================
    // GET BY ID
    // =========================
    @GET
    @Path("/{id}")
    public Customer get(@PathParam("id") Long id) {
        return service.getById(id);
    }

    // =========================
    // UPDATE
    // =========================
    @PUT
    @Path("/{id}")
    public Customer update(@PathParam("id") Long id, @Valid CustomerRequest req) {
        return service.update(id, req);
    }

    // =========================
    // DELETE
    // =========================
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }

    // =========================
    // EXISTS
    // =========================
    @GET
    @Path("/{id}/exists")
    public boolean exists(@PathParam("id") Long id) {
        return service.exists(id);
    }

    // =========================
    // SEARCH BY CITY (paginated)
    // =========================
    @GET
    @Path("/search")
    public PagedResponse<Customer> searchByCity(
            @QueryParam("city") String city,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        if (city == null || city.isBlank()) {
            throw new BadRequestException("city query param is required");
        }

        return service.searchByCityPaged(city, page, size);
    }

    // =========================
    // ACTIVE CUSTOMERS BY CITY (paginated)
    // =========================
    @GET
    @Path("/active")
    public PagedResponse<Customer> activeByCity(
            @QueryParam("city") String city,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        if (city == null || city.isBlank()) {
            throw new BadRequestException("city query param is required");
        }

        return service.activeByCityPaged(city, page, size);
    }

    // =========================
    // FIND BY EMAIL
    // =========================
    @GET
    @Path("/by-email")
    public Customer byEmail(@QueryParam("email") String email) {

        if (email == null || email.isBlank()) {
            throw new BadRequestException("email query param is required");
        }

        Customer c = Customer.findByEmail(email);

        if (c == null) {
            throw new ResourceNotFoundException(
                    "Customer with email " + email + " not found"
            );
        }

        return c;
    }

    // =========================
    // COUNT BY CITY
    // =========================
    @GET
    @Path("/count")
    public long countByCity(@QueryParam("city") String city) {

        if (city == null || city.isBlank()) {
            throw new BadRequestException("city query param is required");
        }

        return Customer.countByCity(city);
    }
}
