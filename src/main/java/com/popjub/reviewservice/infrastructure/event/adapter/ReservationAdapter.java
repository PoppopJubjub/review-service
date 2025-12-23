package com.popjub.reviewservice.infrastructure.event.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.popjub.reviewservice.application.port.ReservationPort;
import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;
import com.popjub.reviewservice.infrastructure.client.ReservationClient;
import com.popjub.reviewservice.infrastructure.client.dto.ReservationValidateRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationAdapter implements ReservationPort {

	private final ReservationClient reservationClient;

	@Override
	public String validateReviewable(
		UUID reservationId,
		Long userId,
		UUID storeId
		) {
		try {
			String rawStatus = reservationClient.validateReservation(
				new ReservationValidateRequest(reservationId, userId, storeId)
			);

			return normalizeStatus(rawStatus);

		} catch (feign.FeignException.NotFound e) {
			// 예약 자체가 없음
			throw new ReviewCustomException(
				ReviewErrorCode.REVIEW_NOT_ALLOWED_BY_RESERVATION_STATUS
			);

		} catch (feign.FeignException.Forbidden e) {
			throw new ReviewCustomException(
				ReviewErrorCode.REVIEW_ACCESS_DENIED
			);

		} catch (feign.FeignException e) {
			throw new ReviewCustomException(
				ReviewErrorCode.REVIEW_NOT_ALLOWED_BY_RESERVATION_STATUS
			);
		}
	}

	private String normalizeStatus(String rawStatus) {
		if (rawStatus == null) {
			throw new ReviewCustomException(
				ReviewErrorCode.REVIEW_NOT_ALLOWED_BY_RESERVATION_STATUS
			);
		}

		// ["CHECKED_IN"] → CHECKED_IN
		return rawStatus
			.replace("[", "")
			.replace("]", "")
			.replace("\"", "")
			.trim();
	}

}
