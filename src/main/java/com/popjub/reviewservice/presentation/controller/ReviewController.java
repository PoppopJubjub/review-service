package com.popjub.reviewservice.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.response.ApiResponse;
import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.PageResponse;
import com.popjub.reviewservice.application.dto.command.AdminBlindCommand;
import com.popjub.reviewservice.application.dto.command.CreateReviewCommand;
import com.popjub.reviewservice.application.dto.result.AdminBlindResult;
import com.popjub.reviewservice.application.dto.result.CreateReviewResult;
import com.popjub.reviewservice.application.dto.result.DeleteReviewResult;
import com.popjub.reviewservice.application.dto.result.ReviewReportResult;
import com.popjub.reviewservice.application.dto.result.SearchReviewResult;
import com.popjub.reviewservice.application.service.ReviewService;
import com.popjub.reviewservice.presentation.dto.request.AdminBlindRequest;
import com.popjub.reviewservice.presentation.dto.request.CreateReviewRequest;
import com.popjub.reviewservice.presentation.dto.response.AdminBlindResponse;
import com.popjub.reviewservice.presentation.dto.response.CreateReviewResponse;
import com.popjub.reviewservice.presentation.dto.response.DeleteReviewResponse;
import com.popjub.reviewservice.presentation.dto.response.ReviewReportResponse;
import com.popjub.reviewservice.presentation.dto.response.SearchReviewResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
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

	@GetMapping
	public ApiResponse<PageResponse<SearchReviewResponse>> getMyReview(
		@RequestHeader("X-User-Id") Long userId,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<SearchReviewResult> result = reviewService.getReviewsByUser(userId, pageable);
		Page<SearchReviewResponse> response = result.map(SearchReviewResponse::from);
		PageResponse<SearchReviewResponse> pageResponse = PageResponse.from(response);

		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}

	@GetMapping("/{reviewId}")
	public ApiResponse<SearchReviewResponse> getReviewById(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable UUID reviewId
	) {
		SearchReviewResult result = reviewService.getReviewById(userId, reviewId);

		SearchReviewResponse response = SearchReviewResponse.from(result);

		return ApiResponse.of(SuccessCode.OK, response);
	}

	@GetMapping("/stores/{storeId}")
	public ApiResponse<PageResponse<SearchReviewResponse>> getReviewsByStore(
		@PathVariable UUID storeId,
		@PageableDefault(
			size = 50,
			sort = "createdAt",
			direction = Sort.Direction.DESC
		) Pageable pageable
	) {
		Page<SearchReviewResult> result = reviewService.getReviewsByStoreId(storeId, pageable);
		Page<SearchReviewResponse> response = result.map(SearchReviewResponse::from);
		PageResponse<SearchReviewResponse> pageResponse = PageResponse.from(response);

		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}

	@DeleteMapping("/{reviewId}")
	public ApiResponse<DeleteReviewResponse> deleteReview(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable UUID reviewId
	) {
		DeleteReviewResult result = reviewService.deleteReview(userId, reviewId);
		return ApiResponse.of(SuccessCode.OK, DeleteReviewResponse.from(result));
	}

	@PatchMapping("/{reviewId}/blind")
	public ApiResponse<AdminBlindResponse> updateBlind(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable UUID reviewId,
		@RequestBody AdminBlindRequest request
	) {
		AdminBlindCommand command = request.toCommand(reviewId);
		AdminBlindResult result = reviewService.updateAdminBlind(command);
		AdminBlindResponse response = AdminBlindResponse.from(result);

		return ApiResponse.of(SuccessCode.OK, response);
	}

	@PostMapping("/{reviewId}/report")
	public ApiResponse<ReviewReportResponse> reportReview(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable UUID reviewId
	) {
		ReviewReportResult result = reviewService.reportReview(userId, reviewId);
		ReviewReportResponse response = ReviewReportResponse.from(result);

		return ApiResponse.of(SuccessCode.OK, response);
	}
}
