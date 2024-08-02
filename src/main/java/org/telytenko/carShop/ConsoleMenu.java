package org.telytenko.carShop;

import org.telytenko.carShop.models.Car;
import org.telytenko.carShop.models.Order;
import org.telytenko.carShop.models.User;
import org.telytenko.carShop.services.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleMenu {
    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuthService authService;
    private AuditService auditService;

    public ConsoleMenu(UserService userService, CarService carService, OrderService orderService, AuthService authService, AuditService auditService) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.authService = authService;
        this.auditService = auditService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("1. Авторизоваться");
            System.out.println("2. Регистрация");
            System.out.println("3. Выйти");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный вариант");
            }
        }
    }

    public void login(Scanner scanner) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(username, password);
            System.out.println("Авторизован как " + user.getUsername());
            auditService.logAction(user.getUsername(), "Login");
            showMainMenu(scanner, user);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void register(Scanner scanner) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите роль (ADMIN, MANAGER, CLIENT): ");
        String roleStr = scanner.nextLine();
        User.Role role = User.Role.valueOf(roleStr.toUpperCase());

        User newUser = new User(username, password, role);
        try {
            userService.addUser(newUser);
            System.out.println("Пользователь успешно зарегистрирован.");
            auditService.logAction("System", "Пользователь зарегистрирован: " + username);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showMainMenu(Scanner scanner, User user) {
        boolean running = true;

        while (running) {
            System.out.println("1. Посмотреть автомобили");
            System.out.println("2. Создать заказ");
            System.out.println("3. Посмотреть заказы");
            if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MANAGER) {
                System.out.println("4. Добавить автомобиль");
                System.out.println("5. Редактировать автомобиль");
                System.out.println("6. Удалить автомобиль");
                System.out.println("7. Просмотр информации о клиентах и сотрудниках");
                System.out.println("8. Фильтрация и поиск");
                System.out.println("9. Аудит действий пользователя");
            }
            if (user.getRole() == User.Role.ADMIN) {
                System.out.println("10. Редактировать пользователя");
            }
            System.out.println("11. Выйти");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewCars();
                    break;
                case 2:
                    createOrder(scanner, user);
                    break;
                case 3:
                    manageOrders(scanner, user);
                    break;
                case 4:
                    if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MANAGER) {
                        addCar(scanner, user);
                    } else {
                        System.out.println("Доступ запрещен.");
                    }
                    break;
                case 5:
                    if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MANAGER) {
                        editCar(scanner, user);
                    } else {
                        System.out.println("Доступ запрещен.");
                    }
                    break;
                case 6:
                    if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MANAGER) {
                        deleteCar(scanner, user);
                    } else {
                        System.out.println("Доступ запрещен.");
                    }
                    break;
                case 7:
                    if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MANAGER) {
                        viewUsersAndEmployees(scanner);
                    } else {
                        System.out.println("Доступ запрещен.");
                    }
                    break;
                case 8:
                    if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MANAGER) {
                        filterAndSearch(scanner);
                    } else {
                        System.out.println("Доступ запрещен.");
                    }
                    break;
                case 9:
                    if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MANAGER) {
                        viewAuditLogs(scanner);
                    } else {
                        System.out.println("Доступ запрещен.");
                    }
                    break;
                case 10:
                    if (user.getRole() == User.Role.ADMIN) {
                        editUser(scanner, user);
                    } else {
                        System.out.println("Доступ запрещен.");
                    }
                    break;
                case 11:
                    authService.logout();
                    auditService.logAction(user.getUsername(), "Logout");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный вариант");
            }
        }
    }

    private void viewCars() {
        List<Car> cars = carService.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("Нет доступных автомобилей.");
        } else {
            for (Car car : cars) {
                System.out.println(car);
            }
        }
    }

    private void createOrder(Scanner scanner, User user) {
        System.out.print("Введите марку автомобиля: ");
        String brand = scanner.nextLine();
        System.out.print("Введите модель автомобиля: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска автомобиля: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        Car car = carService.getCar(brand, model, year);
        if (car == null) {
            System.out.println("Автомобиль не найден.");
            return;
        }

        // Проверка доступности автомобиля
        if (!car.isAvailable()) {
            System.out.println("Автомобиль уже зарезервирован или продан.");
            return;
        }

        System.out.print("Введите тип заказа (PURCHASE, SERVICE): ");
        String typeStr = scanner.nextLine();
        Order.OrderType type = Order.OrderType.valueOf(typeStr.toUpperCase());

        // Начало транзакции
        try {
            carService.beginTransaction();

            // Резервирование автомобиля
            car.setAvailable(false);
            carService.updateCar(car);

            // Создание заказа
            Order order = new Order(user, car, type, Order.OrderStatus.PENDING);
            orderService.createOrder(order);

            // Подтверждение транзакции
            carService.commitTransaction();

            System.out.println("Заказ создан успешно.");
            auditService.logAction(user.getUsername(), "Заказ создан: " + order.getId());
        } catch (Exception e) {
            // Откат транзакции в случае ошибки
            carService.rollbackTransaction();
            System.out.println("Ошибка при создании заказа: " + e.getMessage());
        }
    }

    private void manageOrders(Scanner scanner, User user) {
        boolean running = true;

        while (running) {
            System.out.println("1. Просмотреть все заказы");
            System.out.println("2. Изменить статус заказа");
            System.out.println("3. Отменить заказ");
            System.out.println("4. Назад");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewOrders();
                    break;
                case 2:
                    changeOrderStatus(scanner, user);
                    break;
                case 3:
                    cancelOrder(scanner, user);
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный вариант");
            }
        }
    }

    private void viewOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Нет доступных заказов.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    private void changeOrderStatus(Scanner scanner, User user) {
        System.out.print("Введите ID заказа: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = orderService.getOrder(orderId);
        if (order == null) {
            System.out.println("Заказ не найден.");
            return;
        }

        System.out.print("Введите новый статус заказа (PENDING, COMPLETED, CANCELLED): ");
        String statusStr = scanner.nextLine();
        Order.OrderStatus status = Order.OrderStatus.valueOf(statusStr.toUpperCase());

        order.setStatus(status);
        orderService.updateOrder(order);
        System.out.println("Статус заказа изменен успешно.");
        auditService.logAction(user.getUsername(), "Статус заказа изменен: " + order.getId());
    }

    private void cancelOrder(Scanner scanner, User user) {
        System.out.print("Введите ID заказа: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = orderService.getOrder(orderId);
        if (order == null) {
            System.out.println("Заказ не найден.");
            return;
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderService.updateOrder(order);
        System.out.println("Заказ отменен успешно.");
        auditService.logAction(user.getUsername(), "Заказ отменен: " + order.getId());
    }

    private void addCar(Scanner scanner, User user) {
        System.out.print("Введите марку автомобиля: ");
        String brand = scanner.nextLine();
        System.out.print("Введите модель автомобиля: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска автомобиля: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите цену автомобиля: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Введите состояние автомобиля: ");
        String condition = scanner.nextLine();

        Car car = new Car(brand, model, year, price, condition);
        try {
            carService.addCar(car);
            System.out.println("Автомобиль добавлен успешно.");
            auditService.logAction(user.getUsername(), "Автомобиль добавлен: " + brand + " " + model + " " + year);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void editCar(Scanner scanner, User user) {
        System.out.print("Введите марку автомобиля: ");
        String brand = scanner.nextLine();
        System.out.print("Введите модель автомобиля: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска автомобиля: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        Car car = carService.getCar(brand, model, year);
        if (car == null) {
            System.out.println("Автомобиль не найден.");
            return;
        }

        System.out.print("Введите новую цену автомобиля: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Введите новое состояние автомобиля: ");
        String condition = scanner.nextLine();

        car.setPrice(price);
        car.setCondition(condition);
        carService.updateCar(car);
        System.out.println("Автомобиль обновлен успешно.");
        auditService.logAction(user.getUsername(), "Автомобиль обновлен: " + brand + " " + model + " " + year);
    }

    private void deleteCar(Scanner scanner, User user) {
        System.out.print("Введите марку автомобиля: ");
        String brand = scanner.nextLine();
        System.out.print("Введите модель автомобиля: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска автомобиля: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        try {
            carService.deleteCar(brand, model, year);
            System.out.println("Автомобиль удален успешно.");
            auditService.logAction(user.getUsername(), "Автомобиль удален: " + brand + " " + model + " " + year);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewUsersAndEmployees(Scanner scanner) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Нет зарегистрированных пользователей.");
        } else {
            for (User u : users) {
                System.out.println(u);
            }
        }
    }

    private void filterAndSearch(Scanner scanner) {
        System.out.println("1. Поиск автомобилей");
        System.out.println("2. Поиск заказов");
        System.out.print("Выберите опцию: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                searchCars(scanner);
                break;
            case 2:
                searchOrders(scanner);
                break;
            default:
                System.out.println("Неверный вариант");
        }
    }

    private void searchCars(Scanner scanner) {
        System.out.print("Введите марку автомобиля: ");
        String brand = scanner.nextLine();
        System.out.print("Введите модель автомобиля: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска автомобиля: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите цену автомобиля: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        List<Car> cars = carService.getAllCars();
        List<Car> filteredCars = cars.stream()
                .filter(car -> car.getBrand().equalsIgnoreCase(brand) || car.getModel().equalsIgnoreCase(model) || car.getYear() == year || car.getPrice() == price)
                .collect(Collectors.toList());

        if (filteredCars.isEmpty()) {
            System.out.println("Нет совпадений.");
        } else {
            for (Car car : filteredCars) {
                System.out.println(car);
            }
        }
    }

    private void searchOrders(Scanner scanner) {
        System.out.print("Введите ID заказа: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = orderService.getOrder(orderId);
        if (order == null) {
            System.out.println("Заказ не найден.");
        } else {
            System.out.println(order);
        }
    }

    private void viewAuditLogs(Scanner scanner) {
        List<String> logs = auditService.getLogs();
        if (logs.isEmpty()) {
            System.out.println("Нет записей в журнале аудита.");
        } else {
            for (String log : logs) {
                System.out.println(log);
            }
        }

        System.out.print("Хотите экспортировать журнал аудита в файл? (да/нет): ");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("да")) {
            System.out.print("Введите путь к файлу: ");
            String filePath = scanner.nextLine();
            auditService.exportLogs(filePath);
            System.out.println("Журнал аудита экспортирован успешно.");
        }
    }

    private void editUser(Scanner scanner, User admin) {
        System.out.print("Введите имя пользователя для редактирования: ");
        String username = scanner.nextLine();

        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.print("Введите новый пароль (оставьте пустым, если не хотите менять): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            user.setPassword(password);
        }

        System.out.print("Введите новую роль (оставьте пустым, если не хотите менять): ");
        String roleStr = scanner.nextLine();
        if (!roleStr.isEmpty()) {
            User.Role role = User.Role.valueOf(roleStr.toUpperCase());
            user.setRole(role);
        }

        userService.updateUser(user);
        System.out.println("Пользователь обновлен успешно.");
        auditService.logAction(admin.getUsername(), "Пользователь обновлен: " + user.getUsername());
    }
}
