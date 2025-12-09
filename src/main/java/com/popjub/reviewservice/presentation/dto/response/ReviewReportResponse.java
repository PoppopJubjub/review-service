package com.popjub.reviewservice.presentation.dto.response;

import java.util.UUID;

import com.popjub.reviewservice.application.dto.result.ReviewReportResult;

public record ReviewReportResponse(
	UUID reviewId, int totalReports
) {
	public static ReviewReportResponse from(ReviewReportResult result) {
		return new ReviewReportResponse(
			result.reviewId(),
			result.totalReports()
		);
	}
}
