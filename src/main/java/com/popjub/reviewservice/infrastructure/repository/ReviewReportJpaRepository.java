package com.popjub.reviewservice.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.reviewservice.domain.entity.ReviewReport;

public interface ReviewReportJpaRepository extends JpaRepository<ReviewReport, UUID> {

	boolean existsByReviewIdAndUserId(UUID reviewId, Long userId);
}
