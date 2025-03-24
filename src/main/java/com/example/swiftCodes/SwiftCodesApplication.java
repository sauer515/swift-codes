package com.example.swiftCodes;

import com.example.swiftCodes.csv.CsvConfig;
import com.example.swiftCodes.csv.CsvParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SwiftCodesApplication {
	public static void main(String[] args) {
		SpringApplication.run(SwiftCodesApplication.class, args);
	}

}
