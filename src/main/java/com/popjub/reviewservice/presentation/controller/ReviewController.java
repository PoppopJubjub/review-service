package com.popjub.reviewservice.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.response.ApiResponse;
import com.popjub.common.enums.SuccessCode;
import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;
import com.popjub.reviewservice.application.dto.result.CreateReviewResult;
import com.popjub.reviewservice.application.service.ReviewService;
import com.popjub.reviewservice.presentation.dto.request.CreateReviewRequest;
import com.popjub.reviewservice.presentation.dto.response.CreateReviewResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ApiResponse<CreateReviewResponse> createReview(
		@Valid @RequestBody CreateReviewRequest request,
		@RequestHeader("X-User-Id") Long userId
	) {
		CreateReviewCommand command = request.toCommand(userId);
		CreateReviewResult result = reviewService.createReview(command);
		CreateReviewResponse response = CreateReviewResponse.from(result);

		return ApiResponse.of(SuccessCode.CREATED, response);
	}
}
