package com.foodexpress.restaurant.dto;

import java.util.List;

public class PagedResponse<T> {
    public List<T> data;
    public int page;
    public int size;
    public long totalElements;
    public int totalPages;

    public PagedResponse(List<T> data, int page, int size, long totalElements) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = size <= 0 ? 0 : (int) Math.ceil((double) totalElements / size);
    }
}
