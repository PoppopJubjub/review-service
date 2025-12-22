package com.popjub.reviewservice.infrastructure.s3;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class S3PresignedUploader {

	private final S3Presigner s3Presigner;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String generate(String originalFileName, String dir) {
		String key = dir + "/" + UUID.randomUUID() + "-" + originalFileName;

		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.build();

		PutObjectPresignRequest presignRequest =
			PutObjectPresignRequest.builder()
				.signatureDuration(Duration.ofMinutes(10))
				.putObjectRequest(putObjectRequest)
				.build();

		return s3Presigner.presignPutObject(presignRequest).url().toString();
	}
}


