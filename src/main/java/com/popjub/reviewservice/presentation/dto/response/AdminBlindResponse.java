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
		return AdminBlindResponse.builder()
			.reviewId(result.reviewId())
			.reservationId(result.reservationId())
			.userId(result.userId())
			.storeId(result.storeId())
			.rating(result.rating())
			.content(result.content())
			.reportCount(result.reportCount())
			.currentStatus(result.currentStatus())
			.createdAt(result.createdAt())
			.createdBy(result.createdBy())
			.updatedAt(result.updatedAt())
			.updatedBy(result.updatedBy())
			.build();
	}
}
