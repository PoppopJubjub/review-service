package com.popjub.reviewservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.popjub.reviewservice.infrastructure.client.dto.ReservationValidateRequest;

@FeignClient(name = "reservation-service")
public interface ReservationClient {

	@PostMapping("/internal/reservations/validate")
	String validateReservation(
		@RequestBody ReservationValidateRequest request
	);
}