package com.sivalabs.localstackdemo.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AmazonS3Service {

	private final S3Client s3Client;

	public void listBuckets() {
		ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
		ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
		listBucketsResponse.buckets().forEach(x -> System.out.println(x.name()));
	}

	public void createBucket(String bucketName) {
		CreateBucketRequest bucketRequest = CreateBucketRequest.builder().bucket(bucketName).build();
		s3Client.createBucket(bucketRequest);
	}

	public void upload(String bucketName, String key, InputStream inputStream) throws IOException {
		PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();

		s3Client.putObject(objectRequest, RequestBody.fromBytes(inputStream.readAllBytes()));
	}

	public List<String> listBucketObjects(String bucketName) {
		List<String> files = new ArrayList<>();
		ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucketName).build();

		ListObjectsResponse res = s3Client.listObjects(listObjects);
		List<S3Object> objects = res.contents();
		for (S3Object myValue : objects) {
			String key = myValue.key();
			System.out.println("The name of the key is " + key);
			files.add(key);

		}
		return files;
	}

	public void download(String bucketName, String key) throws IOException {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();

		s3Client.getObject(getObjectRequest).transferTo(new PrintStream(System.out));
	}

}