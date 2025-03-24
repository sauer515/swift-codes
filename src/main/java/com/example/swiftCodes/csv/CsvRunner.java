package com.example.swiftCodes.csv;

import com.example.swiftCodes.service.BankService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class CsvRunner implements ApplicationRunner {
    private final CsvConfig csvConfig;
    private final BankService bankService;
    private final MongoTemplate mongoTemplate;

    public CsvRunner(CsvConfig csvConfig, BankService bankService, MongoTemplate mongoTemplate) {
        this.csvConfig = csvConfig;
        this.bankService = bankService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        String filePath = csvConfig.getFilePath();

        mongoTemplate.getDb().getCollection("banks").drop();

        if (CsvUtils.isCsvValid(filePath)) {
            try {
                bankService.importFromCsv(filePath);
                System.out.println("CSV file parsed successfully");
            } catch (Exception e) {
                throw new RuntimeException("Error parsing CSV file", e);
            }
        }
        else {
            throw new IllegalArgumentException("Invalid CSV file");
        }
    }
}
