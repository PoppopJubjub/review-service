package com.popjub.reviewservice.application.validation;

import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;

public class ImageValidator {

	private static final String ALLOWED_PREFIX =
		"https://popjub-image-bucket.s3.ap-northeast-2.amazonaws.com/";

	public static void validate(String imageUrl) {
		if (imageUrl == null || imageUrl.isBlank()) {
			return; // 이미지 없는 리뷰 허용
		}

		if (!imageUrl.startsWith(ALLOWED_PREFIX)) {
			throw new ReviewCustomException(ReviewErrorCode.INVALID_IMAGE_URL);
		}
	}
}

