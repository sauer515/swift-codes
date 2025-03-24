package com.example.swiftCodes.csv;

import java.io.File;

public class CsvUtils {
    public static boolean isCsvValid(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("File is not a file");
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("File is not readable");
        }
        if (!file.getName().endsWith(".csv")) {
            throw new IllegalArgumentException("File is not a CSV file");
        }
        return true;
    }
}
