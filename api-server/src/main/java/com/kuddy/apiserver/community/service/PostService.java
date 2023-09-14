package com.kuddy.apiserver.community.service;

import com.kuddy.apiserver.community.dto.request.ImageUploadReqDto;
import com.kuddy.apiserver.community.dto.request.PostReqDto;
import com.kuddy.apiserver.community.dto.response.*;
import com.kuddy.apiserver.image.S3Upload;
import com.kuddy.common.comment.exception.NoPostExistException;
import com.kuddy.common.comment.repository.CommentRepository;
import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostImage;
import com.kuddy.common.community.domain.PostType;
import com.kuddy.common.community.exception.EmptyInputFilenameException;
import com.kuddy.common.community.exception.NoAuthorityPostRemove;
import com.kuddy.common.community.exception.WrongImageFormatException;
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
    private final CommentRepository commentRepository;

    @Value("${image.prefix}")
    private String imagePath;

    public ResponseEntity<StatusResponse> savePost(String type, PostReqDto reqDto, Member member) {
        if (type.equals("itinerary")) {
            return saveItineraryPost(reqDto, member);
        } else {
            return saveTalkingboardPost(reqDto, member);
        }
    }

    // Itinerary Feedback 포스팅 저장
    private ResponseEntity<StatusResponse> saveItineraryPost(PostReqDto reqDto, Member member) {
        List<Spot> spots = validateItinerarySpots(reqDto.getSpots());
        String spotStr = changelistToStr(reqDto.getSpots());
        Post post = reqDto.toEntityFromItinerary(reqDto, spotStr, member);
        postRepository.save(post);
        ItineraryResDto itineraryResDto = ItineraryResDto.of(post, spots);

        return ResponseEntity.ok(createStatusResponse(itineraryResDto));
    }

    // Talking Board 포스팅 저장
    private ResponseEntity<StatusResponse> saveTalkingboardPost(PostReqDto reqDto, Member member) {
        Post post;
        if ("joinus".equals(reqDto.getPostType())) {
            post = reqDto.toEntityFromJoinUs(reqDto, member);
        } else {
            post = reqDto.toEntityFromOthers(reqDto, member);
        }
        postRepository.save(post);
        List<String> imageUrls = saveImageUrls(reqDto.getImages(), post);
        TalkingBoardResDto talkingBoardResDto = TalkingBoardResDto.of(post, imageUrls);

        return ResponseEntity.ok(createStatusResponse(talkingBoardResDto));
    }

    @Transactional(readOnly = true)
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

        return ResponseEntity.ok(createStatusResponse(urls));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> getPosts(String type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts;

        if (type.equals("itinerary")) {
            posts = postRepository.findAllByPostTypeOrderByCreatedDateDesc(PostType.ITINERARY, pageRequest);
            List<ItineraryResSimpleDto> itineraryResSimpleDtos = convertToItineraryResDto(posts);
            ItineraryPageResDto resDto = new ItineraryPageResDto(itineraryResSimpleDtos, createPageInfo(posts));
            return ResponseEntity.ok(createStatusResponse(resDto));
        } else {
            List<PostType> types = List.of(PostType.JOIN_US, PostType.OTHERS);
            posts = postRepository.findAllByPostTypeInOrderByCreatedDateDesc(types, pageRequest);
            List<TalkingBoardSimpleDto> talkingBoardSimpleDtos = convertToTalkingBoardResDto(posts);
            TalkingBoardPageResDto resDto = new TalkingBoardPageResDto(talkingBoardSimpleDtos, createPageInfo(posts));
            return ResponseEntity.ok(createStatusResponse(resDto));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NoPostExistException::new);
        if(post.getPostType().equals(PostType.ITINERARY)){
            List<Long> contentIdList = convertStringToList(post.getItinerarySpots());
            List<Spot> spots = spotRepository.findAllByContentIdIn(contentIdList);
            ItineraryResDto resDto = ItineraryResDto.of(post, spots);
            return ResponseEntity.ok(createStatusResponse(resDto));
        }else{
            List<String> urlList = postImageRepository.findAllByPost(post)
                    .stream()
                    .map(PostImage::getFileUrl)
                    .collect(Collectors.toList());

            TalkingBoardResDto resDto =  TalkingBoardResDto.of(post, urlList);
            return ResponseEntity.ok(createStatusResponse(resDto));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> getMyPosts(Member member, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByAuthorOrderByCreatedDateDesc(member, pageRequest);
        List<MyPostResDto> myPostResDtos = new ArrayList<>();

        for (Post post : posts) {
            MyPostResDto myPostResDto = MyPostResDto.builder()
                    .id(post.getId())
                    .postType(post.getPostType().getType())
                    .title(post.getTitle())
                    .createdDate(post.getCreatedDate())
                    .build();
            myPostResDtos.add(myPostResDto);
        }
        MyPostPageResDto resDto = new MyPostPageResDto(myPostResDtos, createPageInfo(posts));

        return ResponseEntity.ok(createStatusResponse(resDto));
    }

    public ResponseEntity<StatusResponse> deletePost(Member member, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NoPostExistException::new);
        checkValidMember(member.getId(), post.getAuthor().getId());
        postRepository.delete(post);
        return ResponseEntity.ok(createStatusResponse("post deleted"));
    }

    private StatusResponse createStatusResponse(Object data) {
        return StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(data)
                .build();
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

    private List<ItineraryResSimpleDto> convertToItineraryResDto(Page<Post> posts) {
        List<ItineraryResSimpleDto> resDtos = new ArrayList<>();
        for (Post post : posts) {
            Integer commentNo = commentRepository.countAllByPostId(post.getId());
            List<Long> contentIdList = convertStringToList(post.getItinerarySpots());
            List<Spot> spots = spotRepository.findAllByContentIdIn(contentIdList);
            resDtos.add(ItineraryResSimpleDto.of(post, spots, commentNo));
        }

        return resDtos;
    }

    private List<TalkingBoardSimpleDto> convertToTalkingBoardResDto(Page<Post> posts) {
        List<TalkingBoardSimpleDto> resDtos = new ArrayList<>();
        for (Post post : posts) {
            Integer commentNo = commentRepository.countAllByPostId(post.getId());
            List<String> urlList = postImageRepository.findAllByPost(post)
                    .stream()
                    .map(PostImage::getFileUrl)
                    .collect(Collectors.toList());

            resDtos.add(TalkingBoardSimpleDto.of(post, urlList, commentNo));
        }
        return resDtos;
    }

    private PageInfo createPageInfo(Page<?> page) {
        return new PageInfo(page.getNumber(), page.getNumberOfElements(), (int) page.getTotalElements(), page.getTotalPages());
    }

    private List<Long> convertStringToList(String str) {
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
            throw new EmptyInputFilenameException();
        }

        String[] validExtensions = {".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG"};
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!Arrays.asList(validExtensions).contains(idxFileName)) {
            throw new WrongImageFormatException();
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

    private void checkValidMember(Long memberId, Long authorId) {
        if (!memberId.equals(authorId)) {
            throw new NoAuthorityPostRemove();
        }
    }


}
