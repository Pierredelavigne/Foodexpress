package com.foodexpress.customer.service;

import com.foodexpress.customer.dto.CustomerRequest;
import com.foodexpress.customer.dto.PagedResponse;
import com.foodexpress.customer.entity.Customer;
import com.foodexpress.customer.exception.DuplicateResourceException;
import com.foodexpress.customer.exception.ResourceNotFoundException;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    // -------------------------
    // READ
    // -------------------------
    public Customer getById(Long id) {
        Customer c = Customer.findById(id);
        if (c == null) throw new ResourceNotFoundException("Customer with id " + id + " not found");
        return c;
    }

    public boolean exists(Long id) {
        return Customer.findByIdOptional(id).isPresent();
    }

    // -------------------------
    // PAGINATION HELPERS
    // -------------------------
    private static int clampSize(int size) {
        return Math.max(1, Math.min(size, 50));
    }

    private static int clampPage(int page) {
        return Math.max(0, page);
    }

    private PagedResponse<Customer> toPagedResponse(PanacheQuery<Customer> query, int page, int size) {
        page = clampPage(page);
        size = clampSize(size);

        long total = query.count();
        List<Customer> list = query.page(Page.of(page, size)).list();
        return new PagedResponse<>(list, page, size, total);
    }

    // -------------------------
    // LIST ALL (paginated)
    // -------------------------
    public PagedResponse<Customer> listAllPaged(int page, int size) {
        PanacheQuery<Customer> query = Customer.findAll();
        return toPagedResponse(query, page, size);
    }

    // -------------------------
    // SEARCH (paginated)
    // -------------------------
    public PagedResponse<Customer> searchByCityPaged(String city, int page, int size) {
        PanacheQuery<Customer> query = Customer.find("city = ?1", city);
        return toPagedResponse(query, page, size);
    }

    public PagedResponse<Customer> activeByCityPaged(String city, int page, int size) {
        PanacheQuery<Customer> query = Customer.find("city = ?1 and active = true", city);
        return toPagedResponse(query, page, size);
    }

    // -------------------------
    // CREATE / UPDATE / DELETE
    // -------------------------
    @Transactional
    public Customer create(CustomerRequest req) {
        if (Customer.find("email", req.email).firstResult() != null) {
            throw new DuplicateResourceException("Customer with email " + req.email + " already exists");
        }

        Customer c = new Customer();
        c.firstName = req.firstName;
        c.lastName = req.lastName;
        c.email = req.email;
        c.phone = req.phone;
        c.address = req.address;
        c.city = req.city;
        c.zipCode = req.zipCode;
        if (req.active != null) c.active = req.active;

        c.persist();
        return c;
    }

    @Transactional
    public Customer update(Long id, CustomerRequest req) {
        Customer c = getById(id);

        if (req.email != null && !req.email.equals(c.email)) {
            if (Customer.find("email", req.email).firstResult() != null) {
                throw new DuplicateResourceException("Customer with email " + req.email + " already exists");
            }
            c.email = req.email;
        }

        c.firstName = req.firstName;
        c.lastName = req.lastName;
        c.phone = req.phone;
        c.address = req.address;
        c.city = req.city;
        c.zipCode = req.zipCode;
        if (req.active != null) c.active = req.active;

        return c;
    }

    @Transactional
    public void delete(Long id) {
        Customer c = getById(id);
        c.delete();
    }

    // -------------------------
    // OPTIONAL: non paginated helpers if you still use them elsewhere
    // -------------------------
    public List<Customer> listAll() {
        return Customer.listAll();
    }

    public List<Customer> searchByCity(String city) {
        return Customer.list("city = ?1", city);
    }
}
