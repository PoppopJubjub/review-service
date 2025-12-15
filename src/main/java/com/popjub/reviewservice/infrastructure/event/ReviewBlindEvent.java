package com.popjub.reviewservice.infrastructure.event;

import java.util.UUID;

public record ReviewBlindEvent (

	UUID reviewId,
	boolean blind,
	double confidence,
	String category,
	String reason
) {}