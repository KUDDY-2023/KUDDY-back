package com.kuddy.apiserver.ticket.dto;

import com.kuddy.common.profile.domain.TicketStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketStatusUpdateDto {
	private String memberEmail;
	private TicketStatus ticketStatus;
}
