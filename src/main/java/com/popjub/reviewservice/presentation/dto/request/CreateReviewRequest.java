package com.popjub.reviewservice.presentation.dto.request;

import java.util.UUID;

import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;

public record CreateReviewRequest (
	UUID reservationId,
	UUID storeId,
	Integer rating,
	String content,
	String imageUrl
) {
	public CreateReviewCommand toCommand(Long userId) {
		return CreateReviewCommand.builder()
			.reservationId(this.reservationId)
			.userId(userId)
			.storeId(this.storeId)
			.rating(this.rating)
			.content(this.content)
			.imageUrl(this.imageUrl)
			.build();
	}
}
