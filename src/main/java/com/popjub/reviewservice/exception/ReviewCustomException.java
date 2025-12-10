package com.popjub.reviewservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ReviewCustomException extends RuntimeException {

	private final ReviewErrorCode errorCode;
	private final String message;
	private final HttpStatus status;

	public ReviewCustomException(ReviewErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
	}
}
