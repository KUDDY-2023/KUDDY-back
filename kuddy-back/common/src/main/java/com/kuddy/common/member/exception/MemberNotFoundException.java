package com.kuddy.common.member.exception;

import com.kuddy.common.exception.custom.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

	public MemberNotFoundException() {
	}

	public MemberNotFoundException(String memberEmail) {
		super("member email : " + memberEmail);
	}
}