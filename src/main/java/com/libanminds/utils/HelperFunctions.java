package com.libanminds.utils;

import java.io.File;

public class HelperFunctions {

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "jpg";
        }
        return name.substring(lastIndexOf + 1);
    }
}
