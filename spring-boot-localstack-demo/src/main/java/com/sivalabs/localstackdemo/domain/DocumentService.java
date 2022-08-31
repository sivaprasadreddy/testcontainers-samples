package com.sivalabs.localstackdemo.domain;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sivalabs.localstackdemo.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {
    private final ApplicationProperties properties;
    private final AmazonS3 amazonS3;

    public List<String> listFiles(String bucketName) {
        List<String> keys = new ArrayList<>();
        S3Objects.inBucket(amazonS3, bucketName).forEach((S3ObjectSummary objectSummary) -> {
            keys.add(objectSummary.getKey());
        });
        return keys;
    }

    public void upload(String filename, InputStream inputStream) {
        log.debug("Uploading image to S3");
        try {
            var metadata = new ObjectMetadata();
            var bytes = IOUtils.toByteArray(inputStream);
            metadata.setContentLength(bytes.length);
            var byteArrayInputStream = new ByteArrayInputStream(bytes);
            var putObjectRequest = new PutObjectRequest(properties.bucketName(), filename, byteArrayInputStream, metadata);
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            log.error("IException: ", e);
            throw new RuntimeException(e);
        }
    }
}
