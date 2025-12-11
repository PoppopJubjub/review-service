package com.popjub.reviewservice.infrastructure.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.popjub.reviewservice.domain.entity.ReviewReport;
import com.popjub.reviewservice.domain.repository.ReviewReportRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewReportRepositoryImpl implements ReviewReportRepository {

	private final ReviewReportJpaRepository jpaRepository;

	@Override
	public ReviewReport save(ReviewReport report) {
		return jpaRepository.save(report);
	}

	@Override
	public boolean existsByReviewIdAndUserId(UUID reviewId, Long userId) {
		return jpaRepository.existsByReviewIdAndUserId(reviewId, userId);
	}
}
