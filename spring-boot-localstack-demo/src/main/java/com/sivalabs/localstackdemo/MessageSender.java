package com.sivalabs.localstackdemo;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    private final SqsTemplate sqsTemplate;

    public MessageSender(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void publish(String queueName, Object payload) {
        sqsTemplate.send(to -> to.queue(queueName).payload(payload));
    }
}
