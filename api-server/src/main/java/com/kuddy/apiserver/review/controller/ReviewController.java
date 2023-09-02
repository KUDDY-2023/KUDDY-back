package com.kuddy.apiserver.review.controller;

import com.kuddy.apiserver.review.dto.ReviewReqDto;
import com.kuddy.apiserver.review.service.ReviewService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<StatusResponse> saveReview(@RequestBody ReviewReqDto reviewReqDto, @AuthUser Member writer) {
        return reviewService.createReview(reviewReqDto, writer);
    }
}