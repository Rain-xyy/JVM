package com.njuse.jvmfinal.util;

import java.io.File;

public class PathUtil {
    public static String transform(String pathName) {
        return pathName.replace(".", File.separator);
    }
}
