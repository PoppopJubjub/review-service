package com.popjub.reviewservice.application.service;

import org.springframework.stereotype.Service;

import com.popjub.reviewservice.infrastructure.s3.S3PresignedUploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewPresignedService {

	private final S3PresignedUploader s3PresignedUploader;

	public String createPresignedUrl(String fileName) {
		return s3PresignedUploader.generate(fileName, "reviews");
	}
}
