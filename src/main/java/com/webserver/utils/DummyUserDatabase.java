package com.webserver.utils;

import java.util.*;

public class DummyUserDatabase {
    private final Map<String, User> usernameToUser = new HashMap<>();

    public void insertUser(User user) {
        validateUser(user);
        if (usernameToUser.containsKey(user.getUsername()))
            throw new IllegalStateException("username is already taken");
        usernameToUser.put(user.getUsername(), user);
    }

    public User readUser(String username) {
        if (!usernameToUser.containsKey(username))
            throw new IllegalStateException("username does not exist");
        return usernameToUser.get(username);
    }

    public void removeUser(String username) {
        if (!usernameToUser.containsKey(username))
            throw new IllegalStateException("username does not exist");
        usernameToUser.remove(username);
    }

    private void validateUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user must not be null");
    }
}
