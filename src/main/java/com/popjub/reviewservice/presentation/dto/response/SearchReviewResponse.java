package com.popjub.reviewservice.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.popjub.reviewservice.application.dto.result.SearchReviewResult;

public record SearchReviewResponse(
	UUID reviewId,
	Long userId,
	UUID storeId,
	UUID reservationId,
	Integer rating,
	String content,
	String imageUrl,
	LocalDateTime createdAt
) {
	public static SearchReviewResponse from(SearchReviewResult result) {
		return new SearchReviewResponse(
			result.reviewId(),
			result.userId(),
			result.storeId(),
			result.reservationId(),
			result.rating(),
			result.content(),
			result.imageUrl(),
			result.createdAt()
		);
	}
}
