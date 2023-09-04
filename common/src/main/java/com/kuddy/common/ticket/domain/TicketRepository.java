package com.kuddy.common.ticket.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.member.domain.Member;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	Optional<Ticket> findById(Long ticketId);

	Optional<Ticket> findByMember(Member member);
}
