package com.example.backend;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import  org.springframework.data.mongodb.core.MongoTemplate;

public class MongoTemplateTests extends BackendApplicationTests{

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testCreateCollection(){
        boolean exists = mongoTemplate.collectionExists("emp");
        if (exists){
            mongoTemplate.dropCollection("emp");
        }
        mongoTemplate.createCollection("emp");
    }

}
