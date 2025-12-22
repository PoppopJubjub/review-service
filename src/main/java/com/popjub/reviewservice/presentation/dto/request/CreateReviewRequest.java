package com.popjub.reviewservice.presentation.dto.request;

import java.util.UUID;

import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;

public record CreateReviewRequest (
	UUID reservationId,
	UUID storeId,
	Integer rating,
	String content
) {
	public CreateReviewCommand toCommand(Long userId, String imageUrl) {
		return new CreateReviewCommand(
			reservationId,
			userId,
			storeId,
			rating,
			content,
			imageUrl
		);
	}
}
