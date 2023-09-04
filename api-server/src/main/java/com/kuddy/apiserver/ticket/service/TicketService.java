package com.kuddy.apiserver.ticket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.ticket.dto.TicketReqDto;
import com.kuddy.apiserver.ticket.dto.TicketStatusUpdateDto;
import com.kuddy.common.meetup.exception.NotAdminException;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.repository.MemberRepository;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.exception.ProfileNotFoundException;
import com.kuddy.common.profile.repository.ProfileRepository;
import com.kuddy.common.ticket.domain.Ticket;
import com.kuddy.common.ticket.domain.TicketRepository;
import com.kuddy.common.ticket.exception.OnlyTravelerRequestException;
import com.kuddy.common.ticket.exception.TicketNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TicketService {
	private final TicketRepository ticketRepository;
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;

	public Long create(Member member, TicketReqDto reqDto){
		validateRoleTypeRequest(member);
		Ticket ticket = reqDto.toEntity(member);
		ticketRepository.save(ticket);
		profileTicketStatusUpdate(member, ticket);
		return ticket.getId();
	}

	private void profileTicketStatusUpdate(Member member, Ticket ticket) {
		Profile profile = profileRepository.findByMember(member).orElseThrow(ProfileNotFoundException::new);
		profile.updateTicketStatus(ticket.getTicketStatus());
	}

	public Ticket update(Member member, TicketReqDto reqDto){
		validateRoleTypeRequest(member);
		Ticket ticket = findByMember(member);
		ticket.updateTicketImageUrl(reqDto.getTicketImageUrl());
		return ticket;
	}

	public Ticket updateStatus(Member admin, TicketStatusUpdateDto reqDto){
		if(!admin.getRoleType().equals(RoleType.ADMIN)){
			throw new NotAdminException();
		}
		Member findMember = memberRepository.findByEmail(reqDto.getMemberEmail()).orElseThrow(MemberNotFoundException::new);
		Ticket ticket = findByMember(findMember);
		ticket.updateTicketStatus(reqDto.getTicketStatus());
		profileTicketStatusUpdate(findMember, ticket);
		return ticket;
	}

	private void validateRoleTypeRequest(Member member){
		if(member.getRoleType().equals(RoleType.KUDDY)){
			throw new OnlyTravelerRequestException();
		}
	}


	@Transactional(readOnly = true)
	public Ticket findByMember(Member member){
		return ticketRepository.findByMember(member).orElseThrow(TicketNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public Ticket findById(Long ticketId){
		return ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);
	}
}
