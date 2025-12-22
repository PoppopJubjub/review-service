package com.popjub.reviewservice.application.validation;

import static com.popjub.reviewservice.exception.ReviewErrorCode.*;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.popjub.common.enums.UserRole;
import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;
import com.popjub.reviewservice.application.port.ReservationPort;
import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.domain.entity.ReviewableStatus;
import com.popjub.reviewservice.domain.repository.ReviewReportRepository;
import com.popjub.reviewservice.domain.repository.ReviewRepository;
import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewValidator {

	private final ReservationPort reservationPort;
	private final ReviewRepository reviewRepository;
	private final ReviewReportRepository reviewReportRepository;

	public void validateCreate(CreateReviewCommand command) {

		String status = reservationPort.validateReviewable(
			command.reservationId(),
			command.userId(),
			command.storeId()
		);
		log.info("reservation status raw = [{}]", status);
		ReviewableStatus reviewableStatus = ReviewableStatus.from(status);

		if (!reviewableStatus.isReviewable()) {
			throw new ReviewCustomException(
				ReviewErrorCode.REVIEW_NOT_ALLOWED_BY_RESERVATION_STATUS
			);
		}

		// 중복 리뷰 방지
		if (reviewRepository.existsByReservationId(command.reservationId())) {
			throw new ReviewCustomException(
				ReviewErrorCode.ALREADY_REVIEWED_RESERVATION
			);
		}
	}

	public void validateReport(Review review, Long userId) {
		review.validateReportable();

		boolean alreadyReported =
			reviewReportRepository.existsByReviewIdAndUserId(
				review.getReviewId(), userId
			);

		if (alreadyReported) {
			throw new ReviewCustomException(ReviewErrorCode.ALREADY_REPORTED_REVIEW);
		}
	}


	public void validateAdminAction(List<String> roles) {

		if (roles == null || !roles.contains(UserRole.ADMIN.name())) {
			throw new ReviewCustomException(ReviewErrorCode.ADMIN_ONLY_ACTION);
		}
	}
}
