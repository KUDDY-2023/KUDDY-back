package com.kuddy.apiserver.review.service;

import com.kuddy.apiserver.review.dto.KuddyReviewListResDto;
import com.kuddy.apiserver.review.dto.ReviewReqDto;
import com.kuddy.apiserver.review.dto.ReviewResDto;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.repository.MemberRepository;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.review.domain.Grade;
import com.kuddy.common.review.domain.Review;
import com.kuddy.common.review.exception.NoAuthorityToDeleteReview;
import com.kuddy.common.review.exception.NotKuddyException;
import com.kuddy.common.review.exception.ReviewNotFoundException;
import com.kuddy.common.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MeetupRepository meetupRepository;
    private final MemberRepository memberRepository;

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

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> findReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
        return returnStatusResponse(ReviewResDto.of(review));
    }

    public ResponseEntity<StatusResponse> deleteReview(Long reviewId, Member member) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
        Meetup meetup = meetupRepository.findById(review.getMeetup().getId()).orElseThrow(MeetupNotFoundException::new);
        meetup.updateReviewed(false);
        meetupRepository.save(meetup);

        if (!member.getId().equals(review.getWriter().getId())) {
            throw new NoAuthorityToDeleteReview();
        }
        reviewRepository.delete(review);

        return returnStatusResponse("review deleted");
    }

    public ResponseEntity<StatusResponse> findReviewOfKuudy(Long kuddyId) {
        Member member = memberRepository.findById(kuddyId).orElseThrow(MemberNotFoundException::new);;
        if(!member.getRoleType().equals(RoleType.KUDDY))
            throw new NotKuddyException();

        List<Review> reviewList = reviewRepository.findAllByMeetupKuddyIdOrderByCreatedDateDesc(kuddyId);
        List<ReviewResDto> reviewResDtoList = new ArrayList<>();
        for(Review review : reviewList) {
            reviewResDtoList.add(ReviewResDto.of(review));
        }

        //등급 퍼센트 계산
        Long count = reviewRepository.countByMeetupKuddyId(kuddyId);
        Double perfect = (double) reviewRepository.countByMeetupKuddyIdAndGrade(kuddyId, Grade.PERFECT) / count * 100;
        Double good = (double) reviewRepository.countByMeetupKuddyIdAndGrade(kuddyId, Grade.GOOD) / count * 100;
        Double disappoint = (double) reviewRepository.countByMeetupKuddyIdAndGrade(kuddyId, Grade.DISAPPOINT) / count * 100;

        KuddyReviewListResDto response = new KuddyReviewListResDto(reviewResDtoList, count, String.format("%.1f%%", perfect), String.format("%.1f%%", good), String.format("%.1f%%", disappoint));

        return returnStatusResponse(response);
    }


    private ResponseEntity<StatusResponse> returnStatusResponse(Object data) {
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(data)
                .build());
    }
}
