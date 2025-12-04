package com.popjub.reviewservice.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.popjub.reviewservice.domain.entity.Review;
import com.popjub.reviewservice.domain.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

	private final ReviewJpaRepository reviewJpaRepository;

	@Override
	public Review save(Review review) {
		return reviewJpaRepository.save(review);
	}

	@Override
	public Optional<Review> findById(UUID id) {
		return reviewJpaRepository.findById(id);
	}
}
