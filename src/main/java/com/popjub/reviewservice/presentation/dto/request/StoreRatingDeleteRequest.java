package com.popjub.reviewservice.presentation.dto.request;

import java.util.UUID;

public record StoreRatingDeleteRequest(UUID storeId, Integer rating) {}
