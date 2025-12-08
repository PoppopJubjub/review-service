package com.popjub.reviewservice.domain.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.popjub.reviewservice.domain.entity.Review;

public interface ReviewRepository {
	Review save(Review review);

	Optional<Review> findById(UUID id);

	Optional<Review> findByReviewIdAndUserId(UUID reviewId, Long userId);

	Page<Review> findAllByUserId(Long userId, Pageable pageable);

	Page<Review> findAllByStoreIdAndIsBlindFalse(UUID storeId, Pageable pageable);

	void delete(Review review);
}
