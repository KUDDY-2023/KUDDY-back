package com.kuddy.apiserver.review.service;

import com.kuddy.apiserver.review.dto.ReviewReqDto;
import com.kuddy.apiserver.review.dto.ReviewResDto;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.review.domain.Grade;
import com.kuddy.common.review.domain.Review;
import com.kuddy.common.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MeetupRepository meetupRepository;

    public ResponseEntity<StatusResponse> createReview(ReviewReqDto reviewReqDto, Member writer) {
        Meetup meetup = meetupRepository.findById(reviewReqDto.getMeetupId()).orElseThrow(MeetupNotFoundException::new);
        meetup.updateReviewed(true);
        meetupRepository.save(meetup);

        Review review = reviewRepository.save(Review.builder()
                .writer(writer)
                .meetup(meetup)
                .grade(Grade.valueOf(reviewReqDto.getGrade().toUpperCase()))
                .content(reviewReqDto.getContent())
                .build());

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.CREATED.getStatusCode())
                .message(StatusEnum.CREATED.getCode())
                .data(ReviewResDto.of(review))
                .build());


    }
}
