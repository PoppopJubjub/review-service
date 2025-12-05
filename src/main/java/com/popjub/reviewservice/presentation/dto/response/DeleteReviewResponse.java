package com.popjub.reviewservice.presentation.dto.response;

import java.util.UUID;

import com.popjub.reviewservice.application.dto.result.DeleteReviewResult;

public record DeleteReviewResponse(UUID reviewId) {
	public static DeleteReviewResponse from(DeleteReviewResult result) {
		return new DeleteReviewResponse(result.reviewId());
	}
}
