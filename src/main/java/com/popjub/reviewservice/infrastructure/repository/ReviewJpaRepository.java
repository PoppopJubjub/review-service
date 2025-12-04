package com.popjub.reviewservice.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.reviewservice.domain.entity.Review;

public interface ReviewJpaRepository extends JpaRepository<Review, UUID> {

	Page<Review> findAllByUserId(Long userId, Pageable pageable);

	Optional<Review> findByReviewIdAndUserId(UUID reviewId, Long userId);

	Page<Review> findAllByStoreId(UUID storeId, Pageable pageable);

}
