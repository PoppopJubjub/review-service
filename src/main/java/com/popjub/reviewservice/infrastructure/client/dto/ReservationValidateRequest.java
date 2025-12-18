package com.popjub.reviewservice.infrastructure.client.dto;

import java.util.UUID;

public record ReservationValidateRequest(
	UUID reservationId,
	Long userId,
	UUID storeId
) {
}
