package com.kuddy.chatserver.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.chatserver.kafka.service.MessageSender;
import com.kuddy.common.chat.domain.Message;
import com.kuddy.common.chat.domain.mongo.Chatting;
import com.kuddy.common.chat.exception.ChatNotFoundException;
import com.kuddy.common.chat.repository.MongoChatRepository;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.exception.NotAuthorException;
import com.kuddy.common.member.repository.MemberRepository;
import com.kuddy.common.util.ConstantUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MeetupStatusService {
	private final MessageSender messageSender;

	private final MemberRepository memberRepository;
	private final MongoChatRepository mongoChatRepository;
	private final MeetupRepository meetupRepository;
	public Meetup cancel(Member member, Meetup meetup){
		validateMember(member, meetup);
		MeetupStatus meetupCancelStatus = meetup.cancelMeetup(member);
		Chatting chatting = mongoChatRepository.findById(meetup.getChatId()).orElseThrow(ChatNotFoundException::new);
		chatting.setMeetStatus(meetupCancelStatus.getName());
		mongoChatRepository.save(chatting);
		Member sender = memberRepository.findById(chatting.getSenderId()).orElseThrow(MemberNotFoundException::new);
		Message message = Message.convertToMessage(chatting,sender);
		messageSender.send(ConstantUtil.KAFKA_TOPIC, message);
		return meetup;
	}
	public void validateMember(Member member, Meetup meetup){
		if(!meetup.getKuddy().getId().equals(member.getId()) && !meetup.getTraveler().getId().equals(member.getId())){
			throw new NotAuthorException();
		}
	}
	@Transactional
	public Meetup findByMeetupId(Long meetupId){
		return meetupRepository.findById(meetupId).orElseThrow(MeetupNotFoundException::new);
	}
}
