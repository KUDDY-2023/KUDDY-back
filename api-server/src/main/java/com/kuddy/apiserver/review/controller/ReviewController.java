package com.kuddy.apiserver.review.controller;

import com.kuddy.apiserver.review.dto.ReviewReqDto;
import com.kuddy.apiserver.review.service.ReviewService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<StatusResponse> saveReview(@RequestBody ReviewReqDto reviewReqDto, @AuthUser Member writer) {
        return reviewService.createReview(reviewReqDto, writer);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<StatusResponse> getReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.findReview(reviewId);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<StatusResponse> deleteReview(@PathVariable Long reviewId, @AuthUser Member member) {
        return reviewService.deleteReview(reviewId, member);
    }

    //Kuddy가 받은 리뷰 리스트 조회
    @GetMapping("/kuddy/{kuddyId}")
    public ResponseEntity<StatusResponse> getReviewOfKuddy(@PathVariable("kuddyId") Long kuddyId) {
        return reviewService.findReviewOfKuudy(kuddyId);
    }

    //Traveler가 작성한 리뷰 리스트 조회
    @GetMapping("/traveler/{travelerId}")
    public ResponseEntity<StatusResponse> getReviewOfTraveler(@PathVariable("travelerId") Long travelerId) {
        return reviewService.findReviewOfTraveler(travelerId);
    }
}