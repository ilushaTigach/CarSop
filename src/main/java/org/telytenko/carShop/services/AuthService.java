package org.telytenko.carShop.services;

import org.telytenko.carShop.models.User;

public interface AuthService {
    User login(String username, String password);
    void logout();
}
