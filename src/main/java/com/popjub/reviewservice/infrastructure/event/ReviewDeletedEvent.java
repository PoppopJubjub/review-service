package com.popjub.reviewservice.infrastructure.event;

import java.util.UUID;

public record ReviewDeletedEvent(
	UUID storeId,
	Integer rating
) {}