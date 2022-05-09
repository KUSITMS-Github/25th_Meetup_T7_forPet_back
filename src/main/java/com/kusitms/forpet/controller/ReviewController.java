package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.Review;
import com.kusitms.forpet.dto.ReviewDto;
import com.kusitms.forpet.dto.ReviewRequestDto;
import com.kusitms.forpet.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰생성
    @PostMapping("/offline-map/{placeid}/{userid}/review")
    public Long createReviewByPlaceId(@PathVariable("placeid") Long placeid,
                                      @PathVariable("userid") Long userid,
                                      @RequestPart(value = "reviewRequestDto") ReviewRequestDto requestDto,
                                      @RequestPart(value = "imageList") List<MultipartFile> multipartFile) {


        Long id = reviewService.createReviewByPlaceId(placeid, userid, requestDto.getStar(), requestDto.getContent(),
                 multipartFile);

        return id;
    }


    //마커 선택(리뷰 정보)
    @GetMapping("/offline-map/{placeid}/marker/review")
    public List<ReviewDto> getReviewByMarker(@PathVariable("placeid") Long placeid) {
        List<Review> list = reviewService.findReviewByPlaceId(placeid);


        //entity -> dto 변환
        List<ReviewDto> collect = list.stream().map(m -> new ReviewDto(m.getId(), m.getUser().getNickname(), m.getUser().getImageUrl(), m.getStar(), m.getContent(),
                        m.getCreateDate(), m.getImageUrlList().split("#")))
                .collect(Collectors.toList());

        return collect;
    }


}


