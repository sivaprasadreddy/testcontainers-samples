package com.sivalabs.localstackdemo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonDynamoDbRepository repo;

    @Test
    void shouldSavePersonAndRetrieve() {
        repo.save(new Person(UUID.randomUUID(), "Siva"));
        repo.save(new Person(UUID.randomUUID(), "Prasad"));

        List<Person> people = repo.findAll();
        assertThat(people).hasSize(2);
    }
}
