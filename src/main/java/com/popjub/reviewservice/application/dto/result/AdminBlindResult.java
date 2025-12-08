package com.popjub.reviewservice.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

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
	String createdBy,
	LocalDateTime updatedAt,
	String updatedBy
) {
}
