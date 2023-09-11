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

	public void setData(String key, String value){
		redisTemplate.opsForValue().set(key, value);
	}
	public String getData(String key){
		return (String) redisTemplate.opsForValue().get(key);
	}
	public boolean existsMember(String key, String roomId){
		String currentValue = getData(key);
		return roomId.equals(currentValue);
	}

	public void deleteData(String key){
		redisTemplate.delete(key);
	}
	public boolean deleteDataIfValueMatches(String key, String expectedValue){
		String currentValue = getData(key);
		if(expectedValue.equals(currentValue)){
			redisTemplate.delete(key);
			return true;  // 삭제 성공
		}
		return false;  // value 불일치로 삭제 실패
	}

	public Optional<String> getRefreshToken(String accountId){
		return Optional.ofNullable(getData("RefreshToken:" + accountId));
	}
	public Optional<String> getBlackList(String accessToken){
		return Optional.ofNullable(getData("BlackList:" + accessToken));
	}



}
