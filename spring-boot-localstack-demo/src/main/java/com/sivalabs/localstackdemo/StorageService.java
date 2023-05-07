package com.sivalabs.localstackdemo;

import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
    private final S3Template s3Template;

    public StorageService(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public void upload(String bucketName, String key, InputStream stream) {
        this.s3Template.upload(bucketName, key, stream);
    }

    public InputStream download(String bucketName, String key) throws IOException {
        return this.s3Template.download(bucketName, key).getInputStream();
    }

    public String downloadAsString(String bucketName, String key) throws IOException {
        try (InputStream is = this.download(bucketName, key)) {
            return new String(is.readAllBytes());
        }
    }
}
