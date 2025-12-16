package com.popjub.reviewservice.domain.entity;

import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;

public enum ReviewableStatus{
	CHECKED_IN;

	public static ReviewableStatus from(String status) {
		return switch (status) {
			case "CHECKED_IN" -> CHECKED_IN;
			default -> throw new ReviewCustomException(
				ReviewErrorCode.REVIEW_NOT_ALLOWED_BY_RESERVATION_STATUS
			);
		};
	}

	public boolean isReviewable() {
		return this == CHECKED_IN;
	}
}
