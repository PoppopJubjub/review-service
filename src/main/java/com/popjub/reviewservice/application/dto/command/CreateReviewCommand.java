package com.popjub.reviewservice.application.dto.command;

import java.util.UUID;

import com.popjub.reviewservice.domain.entity.Review;

import lombok.Builder;

@Builder
public record CreateReviewCommand (
	UUID reservationId,
	Long userId,
	UUID storeId,
	Integer rating,
	String content,
	String imageUrl
) {
	// 엔티티 생성 책임 Command
	public Review toEntity() {
		return Review.of(
			this.reservationId,
			this.userId,
			this.storeId,
			this.rating,
			this.content,
			this.imageUrl
		);
	}
}
