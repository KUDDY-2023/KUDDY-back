package com.kuddy.apiserver.community.service;

import com.kuddy.apiserver.community.dto.request.ImageUploadReqDto;
import com.kuddy.apiserver.community.dto.request.ItineraryReqDto;
import com.kuddy.apiserver.community.dto.request.TalkingboardReqDto;
import com.kuddy.apiserver.community.dto.response.*;
import com.kuddy.apiserver.image.S3Upload;
import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostImage;
import com.kuddy.common.community.domain.PostType;
import com.kuddy.common.community.exception.EmptyInputFilename;
import com.kuddy.common.community.exception.WrongImageFormat;
import com.kuddy.common.community.repository.PostImageRepository;
import com.kuddy.common.community.repository.PostRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.page.PageInfo;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.exception.NoSpotExists;
import com.kuddy.common.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final S3Upload s3Upload;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final SpotRepository spotRepository;

    @Value("${image.prefix}")
    private String imagePath;

    public ResponseEntity<StatusResponse> saveTalkingboardPost(TalkingboardReqDto reqDto, Member member) {
        Post post = null;
        if (reqDto.getPostType().equals("joinus")) {
            post = reqDto.toEntityFromJoinUs(reqDto, member);
        } else {
            post = reqDto.toEntityFromOthers(reqDto, member);
        }
        postRepository.save(post);
        List<String> imageUrls = saveImageUrls(reqDto.getImages(), post);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(TalkingBoardResDto.of(post, imageUrls))
                .build());

    }

    public ResponseEntity<StatusResponse> saveItineraryFeedbackPost(ItineraryReqDto reqDto, Member member) {
        List<Spot> spots = validateItinerarySpots(reqDto.getSpots());

        String spotStr = changelistToStr(reqDto.getSpots());
        Post post = reqDto.toEntity(reqDto, spotStr, member);
        postRepository.save(post);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(ItineraryResDto.of(post, spots))
                .build());
    }

    private List<String> saveImageUrls(List<String> images, Post post) {
        List<String> imageUrls = new ArrayList<>();
        for (String fileName : images) {
            String fileUrl = String.valueOf(imagePath + fileName);
            PostImage postImage = postImageRepository.save(new PostImage(fileUrl, post));
            imageUrls.add(postImage.getFileUrl());
        }

        return imageUrls;
    }


    public ResponseEntity<StatusResponse> getPresignedUrls(Member member, ImageUploadReqDto imageUploadReqDto) {
        List<String> fileNameList = imageUploadReqDto.getImgList();
        log.info(String.valueOf(fileNameList.size()));

        List<ImageUploadResDto> urls = fileNameList.stream().map(raw -> {
            //이미지 파일이 유효한 확장자인지 검사
            isValidFileExtension(raw);
            String fileName = createRandomFileName(raw);
            String presignedUrl = s3Upload.getPresignedUrl(fileName);

            return ImageUploadResDto.builder()
                    .rawFile(raw)
                    .fileName(fileName)
                    .presignedUrl(presignedUrl)
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(urls)
                .build());
    }

    public ResponseEntity<StatusResponse> getTalkingboardList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<PostType> types = List.of(PostType.JOIN_US, PostType.OTHERS);
        Page<Post> posts = postRepository.findAllByPostTypeIn(types, pageRequest);

        List<TalkingBoardResDto> response = new ArrayList<>();
        for (Post post : posts) {
            List<String> urlList = postImageRepository.findAllByPost(post)
                    .stream()
                    .map(PostImage::getFileUrl)
                    .collect(Collectors.toList());

            response.add(TalkingBoardResDto.of(post, urlList));
        }

        PageInfo pageInfo = new PageInfo(page, size, (int) posts.getTotalElements(), posts.getTotalPages());
        TalkingBoardPageResDto resDto = new TalkingBoardPageResDto(response, pageInfo);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(resDto)
                .build());
    }

    public ResponseEntity<StatusResponse> getItineraryFeedbackList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByPostType(PostType.ITINERARY, pageRequest);

        List<ItineraryResDto> response = new ArrayList<>();
        for (Post post : posts) {
            List<Long> contentIdList = convertStringToList(post.getItinerarySpots());
            List<Spot> spots = spotRepository.findAllByContentIdIn(contentIdList);
            response.add(ItineraryResDto.of(post, spots));
        }

        PageInfo pageInfo = new PageInfo(page, size, (int) posts.getTotalElements(), posts.getTotalPages());
        ItineraryPageResDto resDto = new ItineraryPageResDto(response, pageInfo);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(resDto)
                .build());

    }

    public List<Long> convertStringToList(String str) {
        long[] spotList = Stream.of(str.split(",")).mapToLong(Long::parseLong).toArray();
        Long[] spotListBoxed = ArrayUtils.toObject(spotList);
        List<Long> spotListResult = Arrays.asList(spotListBoxed);

        return spotListResult;
    }

    private String createRandomFileName(String rawFile) {
        return UUID.randomUUID() + rawFile;
    }

    private void isValidFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new EmptyInputFilename();
        }

        String[] validExtensions = {".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG"};
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!Arrays.asList(validExtensions).contains(idxFileName)) {
            throw new WrongImageFormat();
        }
    }

    private List<Spot> validateItinerarySpots(List<Long> itineraySpots) {
        if (itineraySpots.size() == 0) {
            throw new NoSpotExists();
        }
        List<Spot> spots = spotRepository.findAllByContentIdIn(itineraySpots);

        return spots;
    }

    private String changelistToStr(List<Long> itineraySpots) {
        return StringUtils.join(itineraySpots, ',');
    }

}
