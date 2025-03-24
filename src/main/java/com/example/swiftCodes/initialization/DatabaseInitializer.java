package com.example.swiftCodes.initialization;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    private final MongoTemplate mongoTemplate;

    public DatabaseInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void cleanDatabase() {

    }
}
