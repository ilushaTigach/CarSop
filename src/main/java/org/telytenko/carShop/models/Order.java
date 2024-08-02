package org.telytenko.carShop.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int id;

    private User client;

    private Car car;

    private OrderType type;

    private OrderStatus status;

    public Order(User client, Car car, OrderType type, OrderStatus status) {
        this.client = client;
        this.car = car;
        this.type = type;
        this.status = status;
    }

    public enum OrderType {
        PURCHASE, SERVICE
    }

    public enum OrderStatus {
        PENDING, COMPLETED, CANCELLED
    }
}
