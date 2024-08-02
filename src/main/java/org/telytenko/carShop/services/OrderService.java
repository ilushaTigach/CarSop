package org.telytenko.carShop.services;

import org.telytenko.carShop.models.Order;

import java.util.List;

public interface OrderService {
    void createOrder(Order order);
    Order getOrder(int orderId);
    void updateOrder(Order order);
    void cancelOrder(int orderId);
    List<Order> getAllOrders();
}
