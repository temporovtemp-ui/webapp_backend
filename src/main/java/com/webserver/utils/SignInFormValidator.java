package com.webserver.utils;

import org.json.JSONObject;

public class SignInFormValidator {
    public static void validateSignInForm(JSONObject form) {
        validateUsername(form);
        validatePassword(form);
    }

    private static void validateUsername(JSONObject form) {
        if (!form.has("username"))
            throw new IllegalArgumentException("form must contain username field");
        if (form.get("username") == null || !(form.get("username") instanceof String username))
            throw new IllegalArgumentException("username must be a non-null string");
    }

    private static void validatePassword(JSONObject form) {
        if (!form.has("password"))
            throw new IllegalArgumentException("form must contain password field");
        if(form.get("password") == null || !(form.get("password") instanceof String password))
            throw new IllegalArgumentException("password must be a non-null string");
    }
}
