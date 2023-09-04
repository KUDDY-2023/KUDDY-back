package com.kuddy.apiserver.ticket.dto;

import javax.validation.constraints.NotBlank;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.TicketStatus;
import com.kuddy.common.ticket.domain.Ticket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketReqDto {

	@NotBlank
	private String ticketImageUrl;

	public Ticket toEntity(Member member){
		return Ticket.builder()
			.ticketImageUrl(ticketImageUrl)
			.ticketStatus(TicketStatus.UNDER_REVIEW)
			.member(member)
			.build();
	}

}
