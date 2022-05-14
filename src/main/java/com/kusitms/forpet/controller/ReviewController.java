package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.Review;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.dto.ReviewDto;
import com.kusitms.forpet.dto.ReviewRequestDto;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.ReviewService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final TokenProvider tokenProvider;

    //리뷰생성
    @PostMapping("/offline-map/{placeid}/review")
    public ApiResponse createReviewByPlaceId(HttpServletRequest request,
                                             @PathVariable("placeid") Long placeid,
                                             @RequestPart(value = "reviewRequestDto") ReviewRequestDto requestDto,
                                             @RequestPart(value = "imageList") List<MultipartFile> multipartFile) {

        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        Long id = reviewService.createReviewByPlaceId(placeid, userid, requestDto.getStar(), requestDto.getContent(),
                 multipartFile);

        return ApiResponse.success("data", id);
    }


    //마커 선택(리뷰 정보)
    @GetMapping("/offline-map/{placeid}/marker/review")
    public ApiResponse getReviewByMarker(@PathVariable("placeid") Long placeid) {
        List<Review> list = reviewService.findReviewByPlaceId(placeid);


        //entity -> dto 변환
        List<ReviewDto> collect = list.stream().map(m -> new ReviewDto(m.getId(), m.getUser().getNickname(), m.getUser().getImageUrl(), m.getStar(), m.getContent(),
                        m.getCreateDate(), m.getImageUrlList().split("#")))
                .collect(Collectors.toList());

        return ApiResponse.success("data", collect);
    }


}


