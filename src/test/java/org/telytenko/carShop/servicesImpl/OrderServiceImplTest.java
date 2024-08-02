package test.java.org.telytenko.carShop.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telytenko.carShop.models.Car;
import org.telytenko.carShop.models.Order;
import org.telytenko.carShop.models.User;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceImplTest {
    private OrderServiceImpl orderService;
    private User client;
    private Car car;
    private Order order;

    @BeforeEach
    public void setUp() {
        orderService = new OrderServiceImpl();
        client = new User("client1", "password1", User.Role.CLIENT);
        car = new Car("Toyota", "Corolla", 2020, 20000, "Good");
        order = new Order(client, car, Order.OrderType.PURCHASE, Order.OrderStatus.PENDING);
    }

    @Test
    public void testCreateOrder() {
        orderService.createOrder(order);
        assertEquals(1, order.getId());
        assertEquals(1, orderService.getAllOrders().size());
        assertTrue(orderService.getAllOrders().contains(order));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(new Order(null, car, Order.OrderType.PURCHASE, Order.OrderStatus.PENDING));
        });
        assertEquals("Не все поля заказа заполнены", exception.getMessage());
    }

    @Test
    public void testGetOrder() {
        assertNull(orderService.getOrder(1));
        orderService.createOrder(order);
        assertEquals(order, orderService.getOrder(1));
    }

    @Test
    public void testUpdateOrder() {
        orderService.createOrder(order);
        order.setStatus(Order.OrderStatus.COMPLETED);
        orderService.updateOrder(order);
        assertEquals(Order.OrderStatus.COMPLETED, orderService.getOrder(1).getStatus());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrder(new Order(2, client, car, Order.OrderType.PURCHASE, Order.OrderStatus.PENDING));
        });
        assertEquals("Заказ с ID 2 не найден.", exception.getMessage());
    }

    @Test
    public void testCancelOrder() {
        orderService.createOrder(order);
        orderService.cancelOrder(1);
        assertEquals(Order.OrderStatus.CANCELLED, orderService.getOrder(1).getStatus());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.cancelOrder(2);
        });
        assertEquals("Заказ с ID 2 не найден.", exception.getMessage());
    }

    @Test
    public void testGetAllOrders() {
        assertTrue(orderService.getAllOrders().isEmpty());
        orderService.createOrder(order);
        List<Order> allOrders = orderService.getAllOrders();
        assertEquals(1, allOrders.size());
        assertTrue(allOrders.contains(order));
    }
}