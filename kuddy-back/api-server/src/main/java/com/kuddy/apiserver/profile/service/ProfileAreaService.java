package com.kuddy.apiserver.profile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.profile.dto.MemberAreaDto;
import com.kuddy.common.profile.domain.Area;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileArea;
import com.kuddy.common.profile.exception.AreaNotFoundException;
import com.kuddy.common.profile.repository.AreaRepository;
import com.kuddy.common.profile.repository.ProfileAreaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProfileAreaService {
	private final AreaRepository areaRepository;
	private final ProfileAreaRepository profileAreaRepository;

	public List<ProfileArea> createProfileArea(Profile profile, List<MemberAreaDto> areaReqDto){
		List<ProfileArea> areas = new ArrayList<>();
		for(MemberAreaDto areaDto : areaReqDto) {
			Area area = findByDistrict(areaDto.getAreaName());
			ProfileArea profileArea = ProfileArea.builder()
				.area(area)
				.profile(profile)
				.build();
			profileAreaRepository.save(profileArea);
			areas.add(profileArea);
		}
		return areas;
	}

	public void updateProfileDistrics(Profile profile, List<MemberAreaDto> areaReqDto){
		for(MemberAreaDto areaDto : areaReqDto) {
			Area area = findByDistrict(areaDto.getAreaName());
			Optional<ProfileArea> optionalProfileArea = findByProfileAndArea(profile, area);
			if(!optionalProfileArea.isPresent()){
				ProfileArea profileArea = ProfileArea.builder()
					.area(area)
					.profile(profile)
					.build();
				profileAreaRepository.save(profileArea);
				profile.updateDistricts(profileArea);
			}

		}
	}
	@Transactional(readOnly = true)
	public Area findByDistrict(String district){
		return areaRepository.findByDistrict(district).orElseThrow(AreaNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public Optional<ProfileArea> findByProfileAndArea(Profile profile, Area area){
		return profileAreaRepository.findByProfileAndArea(profile, area);
	}

}
