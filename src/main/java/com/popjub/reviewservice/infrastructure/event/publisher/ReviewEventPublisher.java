package com.popjub.reviewservice.infrastructure.event.publisher;

import com.popjub.reviewservice.infrastructure.event.ReviewCreateEvent;
import com.popjub.reviewservice.infrastructure.event.ReviewDeletedEvent;
import com.popjub.reviewservice.infrastructure.event.ReviewRatingUpdateEvent;

public interface ReviewEventPublisher {
	void publishReviewCreated(ReviewCreateEvent event);
	void publishReviewRatingUpdated(ReviewRatingUpdateEvent event);
	void publishReviewDeleted(ReviewDeletedEvent event);
}
