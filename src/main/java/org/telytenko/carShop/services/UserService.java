package org.telytenko.carShop.services;

import org.telytenko.carShop.models.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    User getUser(String username);
    void updateUser(User user);
    void deleteUser(String username);
    List<User> getAllUsers();
}
