package test.java.org.telytenko.carShop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telytenko.carShop.models.User;
import org.telytenko.carShop.services.*;

import java.util.Scanner;

import static org.mockito.Mockito.*;

public class ConsoleMenuTest {
    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private AuthService authService;
    private AuditService auditService;
    private ConsoleMenu consoleMenu;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        carService = mock(CarService.class);
        orderService = mock(OrderService.class);
        authService = mock(AuthService.class);
        auditService = mock(AuditService.class);
        scanner = mock(Scanner.class);

        consoleMenu = new ConsoleMenu(userService, carService, orderService, authService, auditService);
    }

    @Test
    public void testLoginFailure() {

        when(authService.login("user1", "wrongpassword")).thenThrow(new IllegalArgumentException("Неправильное имя пользователя или пароль."));
        when(scanner.nextLine()).thenReturn("user1", "wrongpassword");

        consoleMenu.login(scanner);

        verify(authService).login("user1", "wrongpassword");
        verify(auditService, never()).logAction(anyString(), anyString());
    }

    @Test
    public void testRegister() {

        User newUser = new User("newuser", "password", User.Role.CLIENT);
        when(scanner.nextLine()).thenReturn("newuser", "password", "CLIENT");

        consoleMenu.register(scanner);

        verify(userService).addUser(newUser);
        verify(auditService).logAction("System", "Пользователь зарегистрирован: newuser");
    }

    @Test
    public void testRegisterFailure() {
        User newUser = new User("existinguser", "password", User.Role.CLIENT);
        doThrow(new IllegalArgumentException("Пользователь с именем existinguser уже существует."))
                .when(userService).addUser(newUser);
        when(scanner.nextLine()).thenReturn("existinguser", "password", "CLIENT");

        consoleMenu.register(scanner);

        verify(userService).addUser(newUser);
        verify(auditService, never()).logAction(anyString(), anyString());
    }
}