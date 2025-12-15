package com.popjub.reviewservice.infrastructure.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popjub.reviewservice.infrastructure.event.ReviewBlindEvent;
import com.popjub.reviewservice.application.service.ReviewService;
import com.popjub.reviewservice.exception.ReviewCustomException;
import com.popjub.reviewservice.exception.ReviewErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewBlindConsumer {

	private final ReviewService reviewService;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "review.blind", groupId = "review-service-group")
	public void consume(String message) {
		try {
			ReviewBlindEvent event = objectMapper.readValue(message, ReviewBlindEvent.class);

			log.info("[Review] review.blind 이벤트 수신: {}", event);

			reviewService.updateBlind(event.reviewId(), event.blind());

		} catch (Exception e) {
			log.error("[Review] Blind 이벤트 처리 실패: {}", e.getMessage(), e);
			throw new ReviewCustomException(ReviewErrorCode.KAFKA_EVENT_PARSE_FAIL);
		}
	}
}

