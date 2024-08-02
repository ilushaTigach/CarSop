package test.java.org.telytenko.carShop.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telytenko.carShop.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl();
        user1 = new User("user1", "password1", User.Role.CLIENT);
        user2 = new User("user2", "password2", User.Role.ADMIN);
    }

    @Test
    public void testAddUser() {
        userService.addUser(user1);
        assertEquals(1, userService.getAllUsers().size());
        assertEquals(user1, userService.getUser("user1"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user1);
        });
        assertEquals("Пользователь с именем user1 уже существует.", exception.getMessage());
    }

    @Test
    public void testGetUser() {
        assertNull(userService.getUser("user1"));
        userService.addUser(user1);
        assertEquals(user1, userService.getUser("user1"));
    }

    @Test
    public void testUpdateUser() {
        userService.addUser(user1);
        user1.setPassword("newpassword");
        user1.setRole(User.Role.ADMIN);
        userService.updateUser(user1);
        User updatedUser = userService.getUser("user1");
        assertEquals("newpassword", updatedUser.getPassword());
        assertEquals(User.Role.ADMIN, updatedUser.getRole());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(user2);
        });
        assertEquals("Пользователь с именем user2 не существует.", exception.getMessage());
    }

    @Test
    public void testDeleteUser() {
        userService.addUser(user1);
        assertEquals(1, userService.getAllUsers().size());
        userService.deleteUser("user1");
        assertEquals(0, userService.getAllUsers().size());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser("user1");
        });
        assertEquals("Пользователь с именем user1 не существует.", exception.getMessage());
    }

    @Test
    public void testGetAllUsers() {
        assertTrue(userService.getAllUsers().isEmpty());
        userService.addUser(user1);
        userService.addUser(user2);
        List<User> allUsers = userService.getAllUsers();
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }
}