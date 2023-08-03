package com.sivalabs.localstackdemo;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDynamoDbRepository {
    private final DynamoDbTemplate dynamoDbTemplate;

    public PersonDynamoDbRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    public void save(Person p) {
        this.dynamoDbTemplate.save(p);
    }

    public List<Person> findAll() {
        return this.dynamoDbTemplate.scanAll(Person.class).items().stream().toList();
    }
}
