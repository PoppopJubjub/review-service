package com.popjub.reviewservice.infrastructure.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popjub.reviewservice.application.event.ReviewCreateEvent;
import com.popjub.reviewservice.application.event.ReviewDeletedEvent;
import com.popjub.reviewservice.application.event.ReviewEventPublisher;
import com.popjub.reviewservice.application.event.ReviewRatingUpdateEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaReviewEventPublisher implements ReviewEventPublisher {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void publishReviewCreated(ReviewCreateEvent event) {
		publish("review.created", event);
	}

	@Override
	public void publishReviewRatingUpdated(ReviewRatingUpdateEvent event) {
		publish("review.rating.updated", event);
	}

	@Override
	public void publishReviewDeleted(ReviewDeletedEvent event) {
		publish("review.deleted", event);
	}

	private void publish(String topic, Object event) {
		try {
			String message = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(topic, message);
		} catch (Exception e) {
			throw new RuntimeException("Kafka 이벤트 발행 실패", e);
		}
	}
}
