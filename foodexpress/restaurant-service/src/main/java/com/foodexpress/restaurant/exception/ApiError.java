package com.foodexpress.restaurant.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    public LocalDateTime timestamp = LocalDateTime.now();
    public int status;
    public String error;
    public String message;
    public String path;
    public String service;
    public List<Violation> violations;

    public static class Violation {
        public String field;
        public String message;
        public Violation() {}
        public Violation(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
