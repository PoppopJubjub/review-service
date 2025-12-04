package com.popjub.reviewservice.presentation.dto.response;

import java.util.UUID;

import com.popjub.reviewservice.application.dto.result.CreateReviewResult;

public record CreateReviewResponse (
	UUID reviewId,
	Boolean is_blind
) {
	public static CreateReviewResponse from (CreateReviewResult result) {
		return new CreateReviewResponse(
			result.reviewId(),
			result.isBlind()
		);
	}
}
