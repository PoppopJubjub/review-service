package com.popjub.reviewservice.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.popjub.reviewservice.domain.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

}
