package com.webserver.utils;

public class UserPasswordValidator {
    public static void validatePassword(String password) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"))
            throw new IllegalArgumentException("invalid password format or password is too weak");
    }
}
