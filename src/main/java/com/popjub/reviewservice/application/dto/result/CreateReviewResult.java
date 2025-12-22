package com.popjub.reviewservice.application.dto.result;

import java.util.UUID;

import com.popjub.reviewservice.domain.entity.Review;

public record CreateReviewResult (
	UUID reviewId,
	boolean isBlind
){
	public static CreateReviewResult from(Review review) {
		return new CreateReviewResult(
			review.getReviewId(),
			review.isBlind()
		);
	}
}
