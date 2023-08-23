package com.kuddy.common.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;

	public void setData(String key, String value, Long expiredTime){
		redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
	}

	public String getData(String key){
		return (String) redisTemplate.opsForValue().get(key);
	}

	public void deleteData(String key){
		redisTemplate.delete(key);
	}

	public Optional<String> getRefreshToken(String accountId){
		return Optional.ofNullable(getData("RefreshToken:" + accountId));
	}
	public Optional<String> getBlackList(String accessToken){
		return Optional.ofNullable(getData("BlackList:" + accessToken));
	}



}
