package com.kuddy.common.exception.custom;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends ApplicationException {

	public UnAuthorizedException() {
		super(HttpStatus.UNAUTHORIZED);
	}

}
