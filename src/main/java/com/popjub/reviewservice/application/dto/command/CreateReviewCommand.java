package com.popjub.reviewservice.application.dto.command;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CreateReviewCommand (
	UUID reservationId,
	Long userId,
	UUID storeId,
	Integer rating,
	String content,
	String imageUrl
) {}
