package com.popjub.reviewservice.infrastructure.event;

import java.util.UUID;

public record ReviewRatingUpdateEvent(
	UUID storeId,
	Integer rating
) {}
