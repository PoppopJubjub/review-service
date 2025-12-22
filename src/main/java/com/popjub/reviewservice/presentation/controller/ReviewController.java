package com.popjub.reviewservice.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.popjub.common.annotation.CurrentUser;
import com.popjub.common.annotation.RoleCheck;
import com.popjub.common.context.UserContext;
import com.popjub.common.enums.ErrorCode;
import com.popjub.common.enums.UserRole;
import com.popjub.common.exception.CommonException;
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
import com.popjub.reviewservice.application.service.ReviewPresignedService;
import com.popjub.reviewservice.application.service.ReviewService;
import com.popjub.reviewservice.application.service.ReviewPresignedService;
import com.popjub.reviewservice.presentation.dto.request.AdminBlindRequest;
import com.popjub.reviewservice.presentation.dto.request.CreateReviewRequest;
import com.popjub.reviewservice.presentation.dto.response.AdminBlindResponse;
import com.popjub.reviewservice.presentation.dto.response.CreateReviewResponse;
import com.popjub.reviewservice.presentation.dto.response.DeleteReviewResponse;
import com.popjub.reviewservice.presentation.dto.response.ReviewReportResponse;
import com.popjub.reviewservice.presentation.dto.response.SearchReviewResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
//git 확인하고 삭제@@@@
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	private final ReviewPresignedService reviewPresignedService;

	@PostMapping
	public ApiResponse<CreateReviewResponse> createReview(
		@Valid @RequestBody CreateReviewRequest request,
		@CurrentUser UserContext user
	) {
		CreateReviewResult result =
			reviewService.createReview(request, user.getUserId());

		return ApiResponse.of(
			SuccessCode.CREATED,
			CreateReviewResponse.from(result)
		);
	}

	@PostMapping("/presigned")
	public ApiResponse<String> getPresignedUrl(
		@RequestParam String fileName
	) {
		String url = reviewPresignedService.createPresignedUrl(fileName);
		return ApiResponse.of(SuccessCode.OK, url);
	}

	@GetMapping
	public ApiResponse<PageResponse<SearchReviewResponse>> getMyReview(
		@CurrentUser UserContext user,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<SearchReviewResult> result = reviewService.getReviewsByUser(user.getUserId(), pageable);
		Page<SearchReviewResponse> response = result.map(SearchReviewResponse::from);
		PageResponse<SearchReviewResponse> pageResponse = PageResponse.from(response);

		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}

	@GetMapping("/{reviewId}")
	public ApiResponse<SearchReviewResponse> getReviewById(
		@PathVariable UUID reviewId,
		@CurrentUser UserContext user
	) {
		SearchReviewResult result = reviewService.getReviewById(user.getUserId(), reviewId);

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

	@RoleCheck(UserRole.USER)
	@DeleteMapping("/{reviewId}")
	public ApiResponse<DeleteReviewResponse> deleteReview(
		@PathVariable UUID reviewId,
		@CurrentUser UserContext user
	) {
		if (user == null) {
			throw new CommonException(ErrorCode.USER_CONTEXT_NOT_FOUND);
		}
		DeleteReviewResult result = reviewService.deleteReview(user.getUserId(), reviewId);
		return ApiResponse.of(SuccessCode.OK, DeleteReviewResponse.from(result));
	}

	@RoleCheck(UserRole.ADMIN)
	@PatchMapping("/{reviewId}/blind")
	public ApiResponse<AdminBlindResponse> updateBlind(
		@PathVariable UUID reviewId,
		@RequestBody AdminBlindRequest request,
		@CurrentUser UserContext user
	) {
		AdminBlindCommand command = request.toCommand(reviewId);
		AdminBlindResult result = reviewService.updateAdminBlind(command, user.getUserId(),
			user.getRoles());

		return ApiResponse.of(SuccessCode.OK, AdminBlindResponse.from(result));
	}

	@RoleCheck(UserRole.USER)
	@PostMapping("/{reviewId}/report")
	public ApiResponse<ReviewReportResponse> reportReview(
		@PathVariable UUID reviewId,
		@CurrentUser UserContext user
	) {
		ReviewReportResult result = reviewService.reportReview(user.getUserId(), reviewId);
		ReviewReportResponse response = ReviewReportResponse.from(result);

		return ApiResponse.of(SuccessCode.OK, response);
	}
}
