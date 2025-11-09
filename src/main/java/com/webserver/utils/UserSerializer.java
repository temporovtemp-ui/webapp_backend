package com.webserver.utils;

import org.json.JSONObject;

public class UserSerializer {
    public static JSONObject serializeUser(User user) {
        JSONObject userSerialized = new JSONObject();
        userSerialized.put("username", user.getUsername());
        userSerialized.put("password", user.getPassword());
        return userSerialized;
    }
}
