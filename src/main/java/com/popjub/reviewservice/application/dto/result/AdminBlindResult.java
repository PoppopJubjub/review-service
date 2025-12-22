package com.popjub.reviewservice.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

import com.popjub.reviewservice.domain.entity.Review;

import lombok.Builder;

@Builder
public record AdminBlindResult(
	UUID reviewId,
	UUID reservationId,
	Long userId,
	UUID storeId,
	Integer rating,
	String content,
	Integer reportCount,
	boolean currentStatus,
	LocalDateTime createdAt,
	Long createdBy,
	LocalDateTime updatedAt,
	Long updatedBy
) {
	//필드 많아서 Builder
	public static AdminBlindResult from(Review review) {
		return AdminBlindResult.builder()
			.reviewId(review.getReviewId())
			.reservationId(review.getReservationId())
			.userId(review.getUserId())
			.storeId(review.getStoreId())
			.rating(review.getRating())
			.content(review.getContent())
			.reportCount(review.getReportCount())
			.currentStatus(review.isBlind())
			.createdAt(review.getCreatedAt())
			.createdBy(review.getCreatedBy())
			.updatedAt(review.getUpdatedAt())
			.updatedBy(review.getUpdatedBy())
			.build();
	}
}