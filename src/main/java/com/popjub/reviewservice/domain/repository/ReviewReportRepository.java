package com.popjub.reviewservice.domain.repository;

import java.util.UUID;

import com.popjub.reviewservice.domain.entity.ReviewReport;

public interface ReviewReportRepository {

	ReviewReport save(ReviewReport report);

	boolean existsByReviewIdAndUserId(UUID reviewId, Long userId);
}
