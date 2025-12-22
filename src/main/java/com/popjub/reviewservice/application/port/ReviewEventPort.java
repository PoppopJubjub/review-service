package com.popjub.reviewservice.application.port;

import com.popjub.reviewservice.domain.entity.Review;

public interface ReviewEventPort {

	void reviewCreated(Review review);
	void reviewDeleted(Review review);
	void ratingRestored(Review review);
}
