package com.webserver.utils;

import org.json.JSONObject;

public class SignUpFormValidator {
    public static void validateSignUpForm(JSONObject form) {
        validateUsername(form);
        validatePassword(form);
        validateRepeatPassword(form);
        validatePasswordsMatch(form);
    }

    private static void validateUsername(JSONObject form) {
        if (!form.has("username"))
            throw new IllegalArgumentException("form must contain username field");
        if (form.get("username") == null || !(form.get("username") instanceof String username))
            throw new IllegalArgumentException("username must be a non-null string");
        UserUsernameValidator.validateUsername(username);
    }

    private static void validatePassword(JSONObject form) {
        if (!form.has("password"))
            throw new IllegalArgumentException("form must contain password field");
        if(form.get("password") == null || !(form.get("password") instanceof String password))
            throw new IllegalArgumentException("password must be a non-null string");
        UserPasswordValidator.validatePassword(password);
    }

    private static void validateRepeatPassword(JSONObject form) {
        if (!form.has("repeat_password"))
            throw new IllegalArgumentException("form must contain repeat_password field");
        if(form.get("repeat_password") == null || !(form.get("repeat_password") instanceof String))
            throw new IllegalArgumentException("repeat_password must be a non-null string");
    }

    private static void validatePasswordsMatch(JSONObject form) {
        String password = (String)form.get("password");
        String repeat_password = (String)form.get("repeat_password");
        if (!password.equals(repeat_password))
            throw new IllegalArgumentException("password and repeat_password must match");
    }
}
