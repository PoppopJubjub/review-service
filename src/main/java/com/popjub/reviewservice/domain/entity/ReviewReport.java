package com.popjub.reviewservice.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_review_report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReport {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID reportId;

	@Column(nullable = false)
	private UUID reviewId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private LocalDateTime reportedAt;

	@Builder
	private ReviewReport(UUID reviewId, Long userId) {
		this.reviewId = reviewId;
		this.userId = userId;
		this.reportedAt = LocalDateTime.now();
	}

	public static ReviewReport of(UUID reviewId, Long userId) {
		return ReviewReport.builder()
			.reviewId(reviewId)
			.userId(userId)
			.build();
	}
}
