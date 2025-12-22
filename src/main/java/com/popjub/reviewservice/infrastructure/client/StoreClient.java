package com.popjub.reviewservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.popjub.reviewservice.presentation.dto.request.StoreRatingDeleteRequest;
import com.popjub.reviewservice.presentation.dto.request.StoreRatingUpdateRequest;

@FeignClient(name = "store-service")
public interface StoreClient {

	@PostMapping("/internal/stores/{storeId}/reviews/rating")
	void updateStoreRating(
		@PathVariable UUID storeId,
		@RequestBody StoreRatingUpdateRequest request
	);

	@PostMapping("/internal/stores/{storeId}/reviews/rating/delete")
	void deleteStoreRating(
		@PathVariable UUID storeId,
		@RequestBody StoreRatingDeleteRequest request
	);
}
