package com.kuddy.common.response;

import lombok.Getter;

@Getter
public enum StatusEnum {

	OK(200, "SUCCESS"),
	CREATED(201, "CREATED"),
	BAD_REQUEST(400, "BAD_REQUEST"),
	NOT_FOUND(404, "NOT_FOUND"),
	INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");

	Integer statusCode;
	String code;

	StatusEnum(Integer statusCode, String code) {
		this.statusCode = statusCode;
		this.code = code;
	}
}
