package com.popjub.reviewservice.application.port;

import java.util.UUID;

public interface ReservationPort {

	String validateReviewable(UUID reservationId, Long userId);
}
