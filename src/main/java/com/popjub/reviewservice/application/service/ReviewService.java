package com.popjub.reviewservice.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.popjub.reviewservice.application.dto.command.AdminBlindCommand;
import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;
import com.popjub.reviewservice.application.dto.result.AdminBlindResult;
import com.popjub.reviewservice.application.dto.result.CreateReviewResult;
import com.popjub.reviewservice.application.dto.result.DeleteReviewResult;
import com.popjub.reviewservice.application.dto.result.ReviewReportResult;
import com.popjub.reviewservice.application.dto.result.SearchReviewResult;
import com.popjub.reviewservice.application.port.ReviewEventPort;
import com.popjub.reviewservice.application.validation.ImageValidator;
import com.popjub.reviewservice.application.validation.ReviewValidator;
import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.domain.entity.ReviewReport;
import com.popjub.reviewservice.domain.repository.ReviewReportRepository;
import com.popjub.reviewservice.domain.repository.ReviewRepository;
import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;
import com.popjub.reviewservice.infrastructure.client.StoreClient;
import com.popjub.reviewservice.presentation.dto.request.CreateReviewRequest;
import com.popjub.reviewservice.presentation.dto.request.StoreRatingDeleteRequest;
import com.popjub.reviewservice.presentation.dto.request.StoreRatingUpdateRequest;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewEventPort reviewEventPort;
	private final StoreClient storeClient;
	private final ReviewReportRepository reviewReportRepository;
	private final ReviewValidator reviewValidator;

	@Transactional
	public CreateReviewResult createReview (
		CreateReviewRequest request,
		Long userId
	){

		ImageValidator.validate(request.imageUrl());

		CreateReviewCommand command = request.toCommand(userId);

		reviewValidator.validateCreate(command);

		Review review = command.toEntity();
		Review saved = reviewRepository.save(review);

		reviewEventPort.reviewCreated(review);

		return CreateReviewResult.from(saved);
	}

	public Page<SearchReviewResult> getReviewsByUser(Long userId, Pageable pageable) {
		return reviewRepository.findAllByUserId(userId, pageable)
			.map(SearchReviewResult::from);
	}

	public SearchReviewResult getReviewById(Long userId, UUID reviewId) {
		Review review = reviewRepository
			.findByReviewIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new ReviewCustomException(ReviewErrorCode.REVIEW_ACCESS_DENIED));

		return SearchReviewResult.from(review);
	}

	public Page<SearchReviewResult> getReviewsByStoreId(UUID storeId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findAllByStoreIdAndIsBlindFalse(storeId, pageable);
		return reviews.map(SearchReviewResult::from);
	}

	@Transactional
	public DeleteReviewResult deleteReview(Long userId, UUID reviewId) {

		Review review = reviewRepository
			.findByReviewIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new ReviewCustomException(ReviewErrorCode.REVIEW_ACCESS_DENIED));

		review.delete(userId);

		storeClient.deleteStoreRating(
			review.getStoreId(),
			new StoreRatingDeleteRequest(review.getStoreId(), review.getRating())
		);

		/* kafka
		eventPublisher.publishReviewDeleted(
			new ReviewDeletedEvent(review.getStoreId(), review.getRating())
		);
		 */
		return DeleteReviewResult.from(review.getReviewId());
	}

	@Transactional
	public void updateBlind(UUID reviewId, boolean blind) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewCustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

		review.changeBlind(blind);
		// blind == false 인 경우에만 평점 이벤트 발행
		if (!blind) {
			storeClient.updateStoreRating(
				review.getStoreId(),
				new StoreRatingUpdateRequest(review.getStoreId(), review.getRating())
			);
		}
		/* kafka
		if (!blind) {
			eventPublisher.publishReviewRatingUpdated(
				new ReviewRatingUpdateEvent(review.getStoreId(), review.getRating())
			);
		}
		 */
	}

	@Transactional
	public AdminBlindResult updateAdminBlind(
		AdminBlindCommand command,
		Long userId,
		List<String> roles
	) {
		Review review = reviewRepository.findById(command.reviewId())
			.orElseThrow(() -> new ReviewCustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

		reviewValidator.validateAdminAction(roles);

		review.adminChangeBlind(command.blind(), userId);

		return AdminBlindResult.from(review);
	}

	@Transactional
	public ReviewReportResult reportReview(Long userId, UUID reviewId) {

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewCustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

		reviewValidator.validateReport(review, userId);

		ReviewReport report = ReviewReport.of(reviewId, userId);
		reviewReportRepository.save(report);

		review.report();

		return new ReviewReportResult(
			review.getReviewId(),
			review.getReportCount()
		);
	}
}
