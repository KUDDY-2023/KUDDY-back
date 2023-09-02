package com.kuddy.common.member.exception;

import com.kuddy.common.exception.custom.NotFoundException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberNotFoundException extends NotFoundException {

	public MemberNotFoundException(String memberEmail) {
		super("member email : " + memberEmail);
	}
}
