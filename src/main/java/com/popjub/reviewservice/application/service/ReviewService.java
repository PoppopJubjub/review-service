package com.popjub.reviewservice.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;
import com.popjub.reviewservice.application.dto.result.CreateReviewResult;
import com.popjub.reviewservice.application.dto.result.DeleteReviewResult;
import com.popjub.reviewservice.application.dto.result.SearchReviewResult;
import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.domain.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	// 검증처리 구현해야함
	// status : checkIn, storeId 매칭, 중복 작성 불가
	public CreateReviewResult createReview(CreateReviewCommand command) {

		Review review = command.toEntity();

		Review saved = reviewRepository.save(review);

		return CreateReviewResult.builder()
			.reviewId(saved.getReviewId())
			.isBlind(saved.getIsBlind())
			.build();
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
		Page<Review> reviews = reviewRepository.findAllByStoreId(storeId, pageable);
		return reviews.map(SearchReviewResult::from);
	}

	public DeleteReviewResult deleteReview(Long userId, UUID reviewId) {

		Review review = reviewRepository
			.findByReviewIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new RuntimeException("리뷰가 없거나 삭제 권한이 없습니다."));
		review.delete(userId);
		reviewRepository.delete(review);

		return DeleteReviewResult.from(review.getReviewId());
	}
}
