package com.kuddy.common.security.user;

import java.util.Map;

import com.kuddy.common.member.domain.ProviderType;

public interface OAuth2UserInfo {
	String getProviderId();

	String getRegistrationId();

	Map<String, Object> getAttributes();

	ProviderType getProvider();
	String getEmail();
	String getNickname();
	String getProfileImageUrl();

	String getName();
}
