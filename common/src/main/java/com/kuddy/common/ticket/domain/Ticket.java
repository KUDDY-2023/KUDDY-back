package com.kuddy.common.ticket.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.TicketStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ticket extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id", updatable = false)
	private Long id;

	@Column(columnDefinition = "TEXT", name = "ticket_image_url")
	private String ticketImageUrl;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private TicketStatus ticketStatus;

	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;


	@Builder
	public Ticket(Long id, String ticketImageUrl, TicketStatus ticketStatus, Member member) {
		this.id = id;
		this.ticketImageUrl = ticketImageUrl;
		this.ticketStatus = ticketStatus;
		this.member = member;
	}

	public void updateTicketStatus(TicketStatus ticketStatus){
		if(!this.ticketStatus.equals(ticketStatus)){
			this.ticketStatus = ticketStatus;
		}
	}
	public void updateTicketImageUrl(String ticketImageUrl){
		if(!this.ticketImageUrl.equals(ticketImageUrl)){
			this.ticketImageUrl = ticketImageUrl;
		}
	}
}
