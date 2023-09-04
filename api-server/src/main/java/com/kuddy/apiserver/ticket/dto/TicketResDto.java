package com.kuddy.apiserver.ticket.dto;

import com.kuddy.common.profile.domain.TicketStatus;
import com.kuddy.common.ticket.domain.Ticket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketResDto {
	private Long ticketId;
	private String ticketImageUrl;
	private TicketStatus ticketStatus;

	public static TicketResDto of(Ticket ticket) {
		return TicketResDto.builder()
			.ticketId(ticket.getId())
			.ticketImageUrl(ticket.getTicketImageUrl())
			.ticketStatus(ticket.getTicketStatus())
			.build();
	}
}
