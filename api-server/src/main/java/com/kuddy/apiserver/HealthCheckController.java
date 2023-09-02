package com.kuddy.apiserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;

@RequestMapping("/api/health")
@RestController
public class HealthCheckController {

	@GetMapping
	public ResponseEntity<StatusResponse> getSystemTimeMillis() {
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(System.currentTimeMillis())
			.build());
	}
}
