package com.popjub.reviewservice.infrastructure.event.adapter;

import org.springframework.stereotype.Component;

import com.popjub.reviewservice.application.port.ReviewEventPort;
import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.infrastructure.event.ReviewCreateEvent;
import com.popjub.reviewservice.infrastructure.event.ReviewDeletedEvent;
import com.popjub.reviewservice.infrastructure.event.publisher.ReviewEventPublisher;
import com.popjub.reviewservice.infrastructure.event.ReviewRatingUpdateEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaReviewEventAdapter implements ReviewEventPort {

	private final ReviewEventPublisher publisher;

	@Override
	public void reviewCreated(Review review) {
		publisher.publishReviewCreated(
			new ReviewCreateEvent(review.getReviewId(), review.getContent())
		);
	}

	@Override
	public void ratingRestored(Review review) {
		publisher.publishReviewRatingUpdated(
			new ReviewRatingUpdateEvent(review.getStoreId(), review.getRating())
		);
	}

	@Override
	public void reviewDeleted(Review review) {
		publisher.publishReviewDeleted(
			new ReviewDeletedEvent(review.getStoreId(), review.getRating())
		);
	}
}
