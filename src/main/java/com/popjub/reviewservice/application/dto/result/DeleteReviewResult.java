package com.popjub.reviewservice.application.dto.result;

import java.util.UUID;

public record DeleteReviewResult(UUID reviewId) {

	public static DeleteReviewResult from(UUID reviewId) {
		return new DeleteReviewResult(reviewId);
	}
}
