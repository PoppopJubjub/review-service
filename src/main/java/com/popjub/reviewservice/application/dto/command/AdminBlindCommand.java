package com.popjub.reviewservice.application.dto.command;

import java.util.UUID;

public record AdminBlindCommand (
	UUID reviewId,
	boolean blind
) {}
