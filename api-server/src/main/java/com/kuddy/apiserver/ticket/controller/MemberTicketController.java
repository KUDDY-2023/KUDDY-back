package com.kuddy.apiserver.ticket.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kuddy.apiserver.ticket.dto.TicketReqDto;
import com.kuddy.apiserver.ticket.dto.TicketResDto;
import com.kuddy.apiserver.ticket.dto.TicketStatusUpdateDto;
import com.kuddy.apiserver.ticket.service.TicketService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import com.kuddy.common.ticket.domain.Ticket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/members/ticket")
@RequiredArgsConstructor
public class MemberTicketController {
	private final TicketService ticketService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> createTicket(@AuthUser Member member, @Valid @RequestBody TicketReqDto reqDto) {
		Long ticketId = ticketService.create(member, reqDto);
		Ticket ticket = ticketService.findById(ticketId);
		TicketResDto response = TicketResDto.of(ticket);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(ticketId)
			.toUri();

		return ResponseEntity.created(location)
			.body(StatusResponse.builder()
				.status(StatusEnum.CREATED.getStatusCode())
				.message(StatusEnum.CREATED.getCode())
				.data(response)
				.build());
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> getMyTicket(@AuthUser Member member) {
		Ticket ticket = ticketService.findByMember(member);
		TicketResDto response = TicketResDto.of(ticket);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}
	@PatchMapping("/status")
	public ResponseEntity<StatusResponse> updateTicketStatus(@Valid @RequestBody TicketStatusUpdateDto reqDto) {
		Ticket ticket = ticketService.updateStatus(reqDto);
		TicketResDto response = TicketResDto.of(ticket);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

	@PatchMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> updateTicket(@AuthUser Member member, @Valid @RequestBody TicketReqDto reqDto) {
		Ticket ticket = ticketService.update(member, reqDto);
		TicketResDto response = TicketResDto.of(ticket);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

}
