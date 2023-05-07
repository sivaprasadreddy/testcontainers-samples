package com.sivalabs.localstackdemo;

import io.awspring.cloud.s3.S3Template;
import io.awspring.cloud.sqs.annotation.SqsListener;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {
    private final S3Template s3Template;
    private final ApplicationProperties properties;

    public MessageListener(S3Template s3Template, ApplicationProperties properties) {
        this.s3Template = s3Template;
        this.properties = properties;
    }

    @SqsListener(queueNames = {"${app.queue}"})
    public void handle(Message message) {
        String bucketName = this.properties.bucket();
        String key = message.uuid().toString();
        ByteArrayInputStream is = new ByteArrayInputStream(message.content().getBytes(StandardCharsets.UTF_8));
        this.s3Template.upload(bucketName, key, is);
    }
}
