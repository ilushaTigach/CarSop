package org.telytenko.carShop.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;

    private String password;

    private Role role;

    public enum Role {
        ADMIN, MANAGER, CLIENT
    }
}
