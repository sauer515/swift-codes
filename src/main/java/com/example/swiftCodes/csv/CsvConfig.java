package com.example.swiftCodes.csv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class CsvConfig {

    @Value("${csv.file.path}")
    private String filePath;

    public String getFilePath() {
        System.out.println(filePath);
        return filePath;
    }
}
