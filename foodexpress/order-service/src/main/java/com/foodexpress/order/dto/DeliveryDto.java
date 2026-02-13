package com.foodexpress.order.dto;

public class DeliveryDto {
    public Long id;
    public Long orderId;
    public Long driverId;
    public String status;
    public String pickupAddress;
    public String deliveryAddress;
    public String estimatedTime;
}
