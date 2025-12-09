package com.popjub.reviewservice.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.popjub.reviewservice.application.dto.result.AdminBlindResult;

import lombok.Builder;

@Builder
public record AdminBlindResponse(
	UUID reviewId,
	UUID reservationId,
	Long userId,
	UUID storeId,
	Integer rating,
	String content,
	Integer reportCount,
	boolean currentStatus,
	LocalDateTime createdAt,
	String createdBy,
	LocalDateTime updatedAt,
	String updatedBy
) {

	public static AdminBlindResponse from(AdminBlindResult result) {
		return new AdminBlindResponse(
			result.reviewId(),
			result.reservationId(),
			result.userId(),
			result.storeId(),
			result.rating(),
			result.content(),
			result.reportCount(),
			result.currentStatus(),
			result.createdAt(),
			result.createdBy(),
			result.updatedAt(),
			result.updatedBy()
		);
	}
}
