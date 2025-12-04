package com.popjub.reviewservice.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

import com.popjub.reviewservice.domain.entity.Review;

public record SearchReviewResult(
	UUID reviewId,
	Long userId,
	UUID storeId,
	UUID reservationId,
	Integer rating,
	String content,
	String imageUrl,
	LocalDateTime createdAt
){
	public static SearchReviewResult from(Review review) {
		return new SearchReviewResult(
			review.getReviewId(),
			review.getUserId(),
			review.getStoreId(),
			review.getReservationId(),
			review.getRating(),
			review.getContent(),
			review.getImageUrl(),
			review.getCreatedAt()
		);
	}
}
