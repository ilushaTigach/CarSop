package org.telytenko.carShop;

import org.telytenko.carShop.services.*;
import org.telytenko.carShop.servicesImpl.*;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();
        CarService carService = new CarServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        AuthService authService = new AuthServiceImpl(userService);
        AuditService auditService = new AuditServiceImpl();

        ConsoleMenu consoleMenu = new ConsoleMenu(userService, carService, orderService, authService, auditService);
        consoleMenu.run();
    }
}
