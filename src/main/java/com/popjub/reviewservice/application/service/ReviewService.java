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
import com.popjub.reviewservice.application.dto.result.ReviewReportResult;
import com.popjub.reviewservice.application.dto.result.SearchReviewResult;
import com.popjub.reviewservice.application.event.ReviewCreateEvent;
import com.popjub.reviewservice.application.event.ReviewDeletedEvent;
import com.popjub.reviewservice.application.event.ReviewEventPublisher;
import com.popjub.reviewservice.application.event.ReviewRatingUpdateEvent;
import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.domain.entity.ReviewReport;
import com.popjub.reviewservice.domain.repository.ReviewReportRepository;
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
	private final ReviewReportRepository reviewReportRepository;
	// 검증처리 구현해야함
	// status : checkIn, storeId 매칭, 중복 작성 불가
	//	TODO 예약자 본인이 맞는지 검증(Feign Reservation)
	// 	TODO 리뷰 중복 작성 방지
	@Transactional
	public CreateReviewResult createReview(CreateReviewCommand command) {

		if (command.rating() < 1 || command.rating() > 5) {
			throw new IllegalArgumentException("평점은 1~5 사이여야 합니다.");
		}

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
	// TODO 관리자 및 본인만 삭제 가능
	@Transactional
	public DeleteReviewResult deleteReview(Long userId, UUID reviewId) {

		Review review = reviewRepository
			.findByReviewIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new RuntimeException("리뷰가 없거나 삭제 권한이 없습니다."));

		if (review.getDeletedAt() != null) {
			throw new IllegalStateException("이미 삭제된 리뷰입니다.");
		}

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
	// TODO admin 권한 검증
	@Transactional
	public AdminBlindResult updateAdminBlind(AdminBlindCommand command) {

		updateBlind(command.reviewId(), command.blind());

		Review review = reviewRepository.findById(command.reviewId())
			.orElseThrow(() -> new RuntimeException("Review Not Found"));

		//review.setBlind(command.blind());

		return AdminBlindResult.from(review);
	}

	@Transactional
	public ReviewReportResult reportReview(Long userId, UUID reviewId) {

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

		if (review.isBlind()) {
			throw new IllegalStateException("블라인드 처리된 리뷰는 신고할 수 없습니다.");
		}

		// 이미 신고한 적 있는 유저인지 확인
		boolean alreadyReported = reviewReportRepository.existsByReviewIdAndUserId(reviewId, userId);
		if (alreadyReported) {
			throw new RuntimeException("이미 신고한 리뷰입니다.");
		}

		// ReviewReport 테이블에 기록 남기기
		ReviewReport report = ReviewReport.of(reviewId, userId);
		reviewReportRepository.save(report);

		// review 엔티티 신고 횟수 증가
		review.report();

		return new ReviewReportResult(review.getReviewId(), review.getReportCount());
	}
}
