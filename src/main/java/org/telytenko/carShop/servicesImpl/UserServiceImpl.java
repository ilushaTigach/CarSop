package org.telytenko.carShop.servicesImpl;

import org.telytenko.carShop.models.User;
import org.telytenko.carShop.services.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        if (getUser(user.getUsername()) == null) {
            users.add(user);
        } else {
            throw new IllegalArgumentException("Пользователь с именем " + user.getUsername() + " уже существует.");
        }
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
        return user.orElse(null);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = getUser(user.getUsername());
        if (existingUser != null) {
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());
        } else {
            throw new IllegalArgumentException("Пользователь с именем " + user.getUsername() + " не существует.");
        }
    }

    @Override
    public void deleteUser(String username) {
        User user = getUser(username);
        if (user != null) {
            users.remove(user);
        } else {
            throw new IllegalArgumentException("Пользователь с именем " + username + " не существует.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
