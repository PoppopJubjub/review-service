package com.popjub.reviewservice.exception;

import org.springframework.http.HttpStatus;

import com.popjub.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {

	REVIEW_NOT_FOUND("404:리뷰가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	//400
	INVALID_RATING_VALUE("400:평점은 1~5 사이여야 합니다.", HttpStatus.BAD_REQUEST),
	REVIEW_ALREADY_EXISTS("400:이미 해당 예약에 대한 리뷰가 존재합니다.", HttpStatus.BAD_REQUEST),
	ALREADY_REVIEWED_RESERVATION("400:이미 해당 예약에 대한 리뷰가 존재합니다.", HttpStatus.BAD_REQUEST),
	REVIEW_ALREADY_DELETED("400:이미 삭제된 리뷰입니다.", HttpStatus.BAD_REQUEST),
	ALREADY_REPORTED_REVIEW("400:이미 신고한 리뷰입니다.", HttpStatus.BAD_REQUEST),
	CANNOT_REPORT_BLINDED_REVIEW("400:블라인드 처리된 리뷰는 신고할 수 없습니다.", HttpStatus.BAD_REQUEST),
	INVALID_BLIND_STATE("400:이미 동일한 BLIND 상태입니다.", HttpStatus.BAD_REQUEST),
	KAFKA_EVENT_PARSE_FAIL("400:Kafka 메시지 파싱 실패", HttpStatus.BAD_REQUEST),

	// 403
	REVIEW_NOT_ALLOWED_BEFORE_CHECKIN("403:체크인된 사용자만 리뷰 작성가능합니다.", HttpStatus.FORBIDDEN),
	REVIEW_ACCESS_DENIED("403:리뷰에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
	ADMIN_ONLY_ACTION("403:관리자만 수행할 수 있는 작업입니다.", HttpStatus.FORBIDDEN),
	RESERVATION_NOT_MATCHED("403:예약 정보가 일치하지 않습니다.", HttpStatus.FORBIDDEN),
	INVALID_STORE_FOR_REVIEW("StoreId와 일치하지 않습니다.", HttpStatus.FORBIDDEN),
	REVIEW_NOT_ALLOWED_BY_RESERVATION_STATUS("리뷰를 작성할 수 없는 예약 상태입니다.", HttpStatus.FORBIDDEN),

	//503
	RESERVATION_SERVICE_UNAVAILABLE("예약 서비스와 통신할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE);

	private final String message;
	private final HttpStatus status;
}
