package com.popjub.reviewservice.application.dto.result;

import java.util.UUID;

public record ReviewReportResult(
	UUID reviewId,
	int totalReports
) {
}
