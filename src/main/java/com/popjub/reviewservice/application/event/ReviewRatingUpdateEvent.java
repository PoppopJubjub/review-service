package com.popjub.reviewservice.application.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRatingUpdateEvent {
	private UUID storeId;
	private Integer rating;
}
