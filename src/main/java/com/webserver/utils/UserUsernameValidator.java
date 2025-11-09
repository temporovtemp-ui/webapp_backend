package com.webserver.utils;

public class UserUsernameValidator {
    public static void validateUsername(String username) {
        if (!username.matches("^[a-zA-Z0-9_-]{3,20}$"))
            throw new IllegalArgumentException("invalid username format");
    }
}
