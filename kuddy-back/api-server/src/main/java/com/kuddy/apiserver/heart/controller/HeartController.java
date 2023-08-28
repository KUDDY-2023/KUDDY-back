package com.kuddy.apiserver.heart.controller;

import com.kuddy.apiserver.heart.service.HeartService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hearts")
public class HeartController {
    private  final HeartService heartService;

    @PostMapping("/{id}")
    public ResponseEntity<StatusResponse> likeSpot(@PathVariable Long id, @AuthUser Member member) {
        return heartService.likeSpot(id, member);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse> cancelSpot(@PathVariable Long id, @AuthUser Member member) {
        return heartService.cancelSpotLike(id, member);
    }

    @GetMapping("/member")
    public ResponseEntity<StatusResponse> getHeartSpotList(@AuthUser Member member) {
        return heartService.findHeartSpotList(member);
    }
}
