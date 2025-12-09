package com.popjub.reviewservice.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.domain.repository.ReviewRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

	private final ReviewJpaRepository reviewJpaRepository;

	@Override
	public Review save(Review review) {
		return reviewJpaRepository.save(review);
	}

	@Override
	public Optional<Review> findById(UUID id) {
		return reviewJpaRepository.findById(id);
	}

	@Override
	public Page<Review> findAllByUserId(Long userId, Pageable pageable) {
		return reviewJpaRepository.findAllByUserId(userId, pageable);
	}

	@Override
	public Optional<Review> findByReviewIdAndUserId(UUID reviewId, Long userId) {
		return reviewJpaRepository.findByReviewIdAndUserId(reviewId, userId);
	}

	@Override
	public Page<Review> findAllByStoreIdAndIsBlindFalse(UUID storeId, Pageable pageable) {
		return reviewJpaRepository.findAllByStoreIdAndIsBlindFalse(storeId, pageable);
	}

	@Override
	public void delete(Review review) {
		reviewJpaRepository.delete(review);
	}
}
