package org.telytenko.carShop.servicesImpl;

import org.telytenko.carShop.models.Order;
import org.telytenko.carShop.services.OrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private List<Order> orders = new ArrayList<>();
    private int nextOrderId = 1;

    @Override
    public void createOrder(Order order) {
        if (order.getClient() == null || order.getCar() == null || order.getType() == null || order.getStatus() == null) {
            throw new IllegalArgumentException("Не все поля заказа заполнены");
        }
        order.setId(nextOrderId++);
        orders.add(order);
    }

    @Override
    public Order getOrder(int orderId) {
        Optional<Order> order = orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst();
        return order.orElse(null);
    }

    @Override
    public void updateOrder(Order order) {
        Order existingOrder = getOrder(order.getId());
        if (existingOrder != null) {
            existingOrder.setClient(order.getClient());
            existingOrder.setCar(order.getCar());
            existingOrder.setType(order.getType());
            existingOrder.setStatus(order.getStatus());
        } else {
            throw new IllegalArgumentException("Заказ с ID " + order.getId() + " не найден.");
        }
    }

    @Override
    public void cancelOrder(int orderId) {
        Order order = getOrder(orderId);
        if (order != null) {
            order.setStatus(Order.OrderStatus.CANCELLED);
        } else {
            throw new IllegalArgumentException("Заказ с ID " + orderId + " не найден.");
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
}
