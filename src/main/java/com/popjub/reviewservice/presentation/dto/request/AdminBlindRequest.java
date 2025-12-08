package com.popjub.reviewservice.presentation.dto.request;

import java.util.UUID;

import com.popjub.reviewservice.application.dto.command.AdminBlindCommand;

public record AdminBlindRequest(
	boolean blind
) {
	public AdminBlindCommand toCommand(UUID reviewId) {
		return new AdminBlindCommand(reviewId, this.blind);
	}
}
