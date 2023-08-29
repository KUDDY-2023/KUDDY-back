package com.kuddy.apiserver.community.controller;

import com.kuddy.apiserver.community.dto.request.ImageUploadReqDto;
import com.kuddy.apiserver.community.dto.request.ItineraryReqDto;
import com.kuddy.apiserver.community.dto.request.TalkingboardReqDto;
import com.kuddy.apiserver.community.service.PostService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/talkingboard")
    public ResponseEntity<StatusResponse> saveTalkingboard(@RequestBody TalkingboardReqDto reqDto,
                                                           @AuthUser Member member) {
        return postService.saveTalkingboardPost(reqDto, member);
    }

    @PostMapping("/itinerary")
    public ResponseEntity<StatusResponse> saveItineraryFeedback(@RequestBody ItineraryReqDto reqDto,
                                                                @AuthUser Member member) {
        return postService.saveItineraryFeedbackPost(reqDto, member);
    }

    @GetMapping("/talkingboard")
    public ResponseEntity<StatusResponse> getTalkingboard(@RequestParam int page, @RequestParam int size) {
        return postService.getTalkingboardList(page, size);
    }

    @GetMapping("/itinerary")
    public ResponseEntity<StatusResponse> getItineraryFeedback(@RequestParam int page, @RequestParam int size) {
        return postService.getItineraryFeedbackList(page, size);
    }

    @PostMapping("/images")
    public ResponseEntity<StatusResponse> getPresignedUrls(@AuthUser Member member,
                                                           @RequestBody ImageUploadReqDto imageUploadReqDto) {
        return postService.getPresignedUrls(member, imageUploadReqDto);
    }

}