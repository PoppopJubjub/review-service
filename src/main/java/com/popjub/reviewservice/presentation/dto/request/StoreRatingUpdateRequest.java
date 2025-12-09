package com.popjub.reviewservice.presentation.dto.request;

import java.util.UUID;

public record StoreRatingUpdateRequest(
	UUID storeId,
	Integer rating
) {}
