package com.kuddy.apiserver.meetup.service;

import com.kuddy.apiserver.meetup.dto.MeetupListResDto;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.ProviderType;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MeetupReviewService {

    private final MeetupRepository meetupRepository;

    public ResponseEntity<StatusResponse> checkReviewByMember(Member member) {
        //현재 시간(UTC)
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        log.info("현재 시간: " + now);
        List<Meetup> meetupList = meetupRepository.findAllByNotReviewed(now.minusDays(3), now, member.getId(), false, MeetupStatus.PAYED);
        ProviderType providerType = member.getProviderType();
        MeetupListResDto response = MeetupListResDto.from(meetupList, member, providerType);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(response)
                .build());
    }
}
