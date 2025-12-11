package com.popjub.reviewservice.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Id;

import com.popjub.common.entity.BaseEntity;
import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_review")
@SQLDelete(sql = "UPDATE p_review SET deleted_at = NOW() WHERE review_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID reviewId;

	@Column(nullable = false)
	private UUID reservationId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private UUID storeId;

	@Column(nullable = false)
	private Integer rating;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(length = 255)
	private String imageUrl;

	@Column(nullable = false)
	private Integer reportCount = 0;

	@Column(nullable = false)
	private boolean isBlind = false;

	private static void validateRating(int rating) {
		if (rating < 1 || rating > 5) {
			throw new ReviewCustomException(ReviewErrorCode.INVALID_RATING_VALUE);
		}
	}

	@Builder(access = AccessLevel.PRIVATE)
	private Review(
		UUID reservationId,
		Long userId,
		UUID storeId,
		Integer rating,
		String content,
		String imageUrl
	) {
		validateRating(rating); //생성시점 검증
		this.reservationId = reservationId;
		this.userId = userId;
		this.storeId = storeId;
		this.rating = rating;
		this.content = content;
		this.imageUrl = imageUrl;
		this.reportCount = 0;
		this.isBlind = false;
	}

	public static Review of(
		UUID reservationId,
		Long userId,
		UUID storeId,
		Integer rating,
		String content,
		String imageUrl
	) {
		return Review.builder()
			.reservationId(reservationId)
			.userId(userId)
			.storeId(storeId)
			.rating(rating)
			.content(content)
			.imageUrl(imageUrl)
			.build();
	}

	public void validateReportable() {
		if (this.isBlind) {
			throw new ReviewCustomException(ReviewErrorCode.CANNOT_REPORT_BLINDED_REVIEW);
		}
	}

	public void report() {
		this.reportCount += 1;
	}

	public void delete(Long userId) {
		super.softDelete(userId);
	}

	public void changeBlind(boolean newBlind) {
		if (this.isBlind == newBlind) {
			throw new ReviewCustomException(ReviewErrorCode.INVALID_BLIND_STATE);
		}
		this.isBlind = newBlind;
	}
}