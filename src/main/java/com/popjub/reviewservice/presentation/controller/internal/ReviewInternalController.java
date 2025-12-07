package com.popjub.reviewservice.presentation.controller.internal;

import java.util.UUID;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.reviewservice.application.service.ReviewService;
import com.popjub.reviewservice.presentation.dto.request.internal.BlindUpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/internal/reviews")
@RequiredArgsConstructor
public class ReviewInternalController {

	private final ReviewService reviewService;

	@PatchMapping("/{id}/blind")
	public void updateBlind(
		@PathVariable UUID id,
		@RequestBody BlindUpdateRequest request
	) {
		log.info("AI 블라인드 true: id={}, blind={}", id, request.blind());
		reviewService.updateBlind(id, request.blind());
	}
}
