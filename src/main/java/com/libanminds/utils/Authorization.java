package com.libanminds.utils;
import com.libanminds.models.User;

import java.util.ArrayList;

public class Authorization {
    public static User loggedInUser;

    public static ArrayList<String> authorized;

    public static void Authorize(ArrayList<String> data) {
        authorized = data;
    }
}