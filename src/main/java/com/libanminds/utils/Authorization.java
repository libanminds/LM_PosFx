package com.libanminds.utils;

import com.libanminds.models.User;

import java.util.ArrayList;
import java.util.List;

public class Authorization {
    public static User loggedInUser;
    private static final List<String> permissions = new ArrayList<>();

    public static void authorize(List<String> data) {
        permissions.clear();
        permissions.addAll(data);
    }

    public static boolean can(String permission) {
        return permissions.contains(permission);
    }

    public static boolean cannot(String permission) {
        return !permissions.contains(permission);
    }

    private Authorization() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}