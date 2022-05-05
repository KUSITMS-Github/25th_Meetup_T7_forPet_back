package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.Review;
import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.dto.CategoryDto;
import com.kusitms.forpet.dto.ReviewDto;
import com.kusitms.forpet.dto.ReviewRequestDto;
import com.kusitms.forpet.repository.APIRepository;
import com.kusitms.forpet.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final APIRepository apiRepository;

    //리뷰생성
    @PostMapping("/offline-map/{placeid}/review")
    public Long createReviewByPlaceId(@PathVariable("placeid") Long placeid, @RequestBody ReviewRequestDto requestDto){
        /**
         * 작성회원 찾기
         */



        Long id = reviewService.createReviewByPlaceId(placeid, requestDto.getStar(), requestDto.getContent(), requestDto.getWriter());

        return id;
    }


    //카테고리 선택
    @GetMapping("/offline-map/category")
    public List<CategoryDto> getPlaceInfoByCategory(@RequestParam(value = "category")String category) {
        List<placeInfo> list = apiRepository.findAllByCategory(category);

        //entity -> dto 변환
        List<CategoryDto> collect = list.stream().map(m -> new CategoryDto(m.getId(), m.getName(), m.getCategory(), m.getAddress(), m.getStarAvg(), m.getReviewCnt()))
                .collect(Collectors.toList());

        return collect;

    }

    //마커 선택(가게 정보)
    @GetMapping("/offline-map/{placeid}/marker")
    public CategoryDto getPlaceInfoByMarker(@PathVariable("placeid") Long placeid) {
        placeInfo placeInfo = apiRepository.findById(placeid).get();
        CategoryDto categoryDto = new CategoryDto(placeInfo.getId(), placeInfo.getName(), placeInfo.getCategory(),
                                            placeInfo.getAddress(), placeInfo.getStarAvg(), placeInfo.getReviewCnt());

        return categoryDto;
    }

    //마커 선택(리뷰 정보)
    @GetMapping("offline-map/{placeid}/marker/review")
    public List<ReviewDto> getReviewByMarker(@PathVariable("placeid") Long placeid) {
        List<Review> list = reviewService.findReviewByPlaceId(placeid);

        //entity -> dto 변환
        List<ReviewDto> collect = list.stream().map(m -> new ReviewDto(m.getId(), m.getStar(), m.getContent(), m.getWriter(), m.getCreateDate()))
                .collect(Collectors.toList());

        return collect;

    }
}
