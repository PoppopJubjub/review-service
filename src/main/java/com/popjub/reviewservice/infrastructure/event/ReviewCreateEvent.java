package com.popjub.reviewservice.infrastructure.event;

import java.util.UUID;

public record ReviewCreateEvent(
	UUID reviewId,
	String content
) {}