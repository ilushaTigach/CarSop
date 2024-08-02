package org.telytenko.carShop.servicesImpl;

import org.telytenko.carShop.models.User;
import org.telytenko.carShop.services.AuthService;
import org.telytenko.carShop.services.UserService;

public class AuthServiceImpl implements AuthService {
    private UserService userService;
    private User currentUser;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User login(String username, String password) {
        User user = userService.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        } else {
            throw new IllegalArgumentException("Неправильное имя пользователя или пароль.");
        }
    }

    @Override
    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
