package com.webserver.utils;

import org.json.JSONObject;

public class SignUpFormValidator {
    public static void validateSignUpForm(JSONObject form) {
        validateUsername(form);

        if (!form.has("password"))
            throw new IllegalArgumentException("form must contain password field");
        if (!form.has("repeat_password"))
            throw new IllegalArgumentException("form must contain repeat_password field");
    }

    private static void validateUsername(JSONObject form) {
        if (!form.has("username"))
            throw new IllegalArgumentException("form must contain username field");
        if (form.get("username") == null || !(form.get("username") instanceof String))
            throw new IllegalArgumentException("username must be a non-null string");
        String username = (String)form.get("username");
        if (!username.matches("^[a-zA-Z0-9_-]{3,20}$"))
            throw new IllegalArgumentException("invalid username format");

    }
}
