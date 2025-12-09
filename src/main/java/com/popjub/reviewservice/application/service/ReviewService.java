package com.popjub.reviewservice.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popjub.reviewservice.application.dto.command.AdminBlindCommand;
import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;
import com.popjub.reviewservice.application.dto.result.AdminBlindResult;
import com.popjub.reviewservice.application.dto.result.CreateReviewResult;
import com.popjub.reviewservice.application.dto.result.DeleteReviewResult;
import com.popjub.reviewservice.application.dto.result.SearchReviewResult;
import com.popjub.reviewservice.application.event.ReviewCreateEvent;
import com.popjub.reviewservice.application.event.ReviewDeletedEvent;
import com.popjub.reviewservice.application.event.ReviewEventPublisher;
import com.popjub.reviewservice.application.event.ReviewRatingUpdateEvent;
import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.domain.repository.ReviewRepository;
import com.popjub.reviewservice.infrastructure.client.StoreClient;
import com.popjub.reviewservice.presentation.dto.request.StoreRatingDeleteRequest;
import com.popjub.reviewservice.presentation.dto.request.StoreRatingUpdateRequest;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewEventPublisher eventPublisher;
	private final StoreClient storeClient;
	// 검증처리 구현해야함
	// status : checkIn, storeId 매칭, 중복 작성 불가
	@Transactional
	public CreateReviewResult createReview(CreateReviewCommand command) {

		Review review = command.toEntity();
		Review saved = reviewRepository.save(review);

		eventPublisher.publishReviewCreated(
			new ReviewCreateEvent(saved.getReviewId(), saved.getContent())
		);

		return CreateReviewResult.from(saved);
	}

	public Page<SearchReviewResult> getReviewsByUser(Long userId, Pageable pageable) {

		Page<Review> reviews = reviewRepository.findAllByUserId(userId, pageable);

		return reviews.map(SearchReviewResult::from);
	}

	public SearchReviewResult getReviewById(Long userId, UUID reviewId) {
		Review review = reviewRepository
			.findByReviewIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않거나 접근 권한이 없습니다."));

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
			.orElseThrow(() -> new RuntimeException("리뷰가 없거나 삭제 권한이 없습니다."));
		review.delete(userId);
		//reviewRepository.delete(review);

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
			.orElseThrow(() -> new RuntimeException("Review not Found"));

		review.setBlind(blind);
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
	public AdminBlindResult updateAdminBlind(AdminBlindCommand command) {

		updateBlind(command.reviewId(), command.blind());

		Review review = reviewRepository.findById(command.reviewId())
			.orElseThrow(() -> new RuntimeException("Review Not Found"));

		//review.setBlind(command.blind());

		return AdminBlindResult.from(review);
	}
}
