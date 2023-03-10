package com.sivalabs.localstackdemo.domain;

import com.sivalabs.localstackdemo.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

	private final ApplicationProperties properties;

	private final AmazonS3Service amazonS3Service;

	public List<String> listFiles(String bucketName) {
		return amazonS3Service.listBucketObjects(bucketName);
	}

	public void upload(String filename, InputStream inputStream) {
		log.debug("Uploading image to S3");
		try {
			amazonS3Service.upload(properties.bucketName(), filename, inputStream);
		}
		catch (Exception e) {
			log.error("IException: ", e);
			throw new RuntimeException(e);
		}
	}

}
