package com.kuddy.common.security.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.MemberStatus;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.repository.MemberRepository;
import com.kuddy.common.security.user.GoogleUserInfo;
import com.kuddy.common.security.user.KakaoUserInfo;
import com.kuddy.common.security.user.OAuth2UserInfo;
import com.kuddy.common.security.user.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final MemberRepository memberRepository;


	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("getAttributes : {}", oAuth2User.getAttributes());

		OAuth2UserInfo oAuth2UserInfo = null;

		String provider = userRequest.getClientRegistration().getRegistrationId();

		if(provider.equals("google")) {
			log.info("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo( oAuth2User.getAttributes() );
		} else if(provider.equals("kakao")) {
			log.info("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo( oAuth2User.getAttributes() );
		}

		Member member = saveOrUpdate(oAuth2UserInfo);
		return new PrincipalDetails(member, oAuth2User.getAttributes());

	}

	// 혹시 이미 저장된 정보라면, update 처리
	private Member saveOrUpdate(OAuth2UserInfo oAuth2UserInfo) {
		Member member = memberRepository.findByUsername(oAuth2UserInfo.getName())
			.map(entity -> {
				entity.updateMember(oAuth2UserInfo.getEmail());
				return entity;
			})
			.orElseGet(() -> {
				if (!memberRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
					String nickname = oAuth2UserInfo.getNickname();
					if(memberRepository.existsByNickname(nickname)){
						nickname = String.valueOf(System.currentTimeMillis());
					}
					return Member.builder()
						.nickname(nickname)
						.email(oAuth2UserInfo.getEmail())
						.profileImageUrl(oAuth2UserInfo.getProfileImageUrl())
						.username(oAuth2UserInfo.getName())
						.providerType(oAuth2UserInfo.getProvider())
						.roleType(RoleType.MEMBER)
						.status(MemberStatus.REGISTERED)
						.build();
				} else {
					return null;
				}
			});

		if (member == null) {
			member = memberRepository.findByEmail(oAuth2UserInfo.getEmail()).orElseThrow(MemberNotFoundException::new);
			member.updateMember(oAuth2UserInfo.getEmail());
			return member;
		}

		return memberRepository.save(member);
	}



}
