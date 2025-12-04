package com.popjub.reviewservice.application.dto.result;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CreateReviewResult (
	UUID reviewId,
	Boolean isBlind
){}
