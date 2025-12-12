package com.popjub.reviewservice.application.event;

import java.util.UUID;

public record ReviewCreateEvent(
	UUID reviewId,
	String content
) {}