package com.popjub.reviewservice.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.reviewservice.domain.entity.Review;

public interface ReviewJpaRepository extends JpaRepository<Review, UUID> {
}
