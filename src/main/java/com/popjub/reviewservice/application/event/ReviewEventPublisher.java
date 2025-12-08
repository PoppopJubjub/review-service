package com.popjub.reviewservice.application.event;

public interface ReviewEventPublisher {
	void publishReviewCreated(ReviewCreateEvent event);
	void publishReviewRatingUpdated(ReviewRatingUpdateEvent event);
}
