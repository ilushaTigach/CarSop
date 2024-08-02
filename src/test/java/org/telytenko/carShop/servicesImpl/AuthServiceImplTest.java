package test.java.org.telytenko.carShop.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telytenko.carShop.models.User;
import org.telytenko.carShop.services.UserService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
    private UserService userService;
    private AuthServiceImpl authService;
    private User user;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        authService = new AuthServiceImpl(userService);
        user = new User("user1", "password1", User.Role.CLIENT);
    }

    @Test
    public void testLoginSuccess() {
        when(userService.getUser("user1")).thenReturn(user);
        User loggedInUser = authService.login("user1", "password1");
        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
        assertEquals(user, authService.getCurrentUser());
    }

    @Test
    public void testLoginFailure() {
        when(userService.getUser("user1")).thenReturn(user);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login("user1", "wrongpassword");
        });
        assertEquals("Неправильное имя пользователя или пароль.", exception.getMessage());
        assertNull(authService.getCurrentUser());
    }

    @Test
    public void testLogout() {
        when(userService.getUser("user1")).thenReturn(user);
        authService.login("user1", "password1");
        assertNotNull(authService.getCurrentUser());
        authService.logout();
        assertNull(authService.getCurrentUser());
    }

    @Test
    public void testGetCurrentUser() {
        assertNull(authService.getCurrentUser());
        when(userService.getUser("user1")).thenReturn(user);
        authService.login("user1", "password1");
        assertEquals(user, authService.getCurrentUser());
    }
}