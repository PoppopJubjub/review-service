package com.popjub.reviewservice.domain.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.popjub.reviewservice.domain.entity.Review;

public interface ReviewRepository {
	Review save(Review review);
	Optional<Review> findById(UUID id);
}
