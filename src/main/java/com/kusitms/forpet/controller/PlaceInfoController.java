package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.dto.OfflineMapDto;
import com.kusitms.forpet.dto.response.ApiResponse;
import com.kusitms.forpet.repository.APIRep;
import com.kusitms.forpet.service.APIService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlaceInfoController {
    private final APIService apiService;
    private final APIRep apiRepository;

    @GetMapping("/offline-map")
    public ApiResponse getApi() {

        List<placeInfo> list = apiService.findAll();
        //entity -> dto 변환
        List<OfflineMapDto.placeDto> collect = list.stream().map(m -> new OfflineMapDto.placeDto(m.getId(), m.getCategory(), m.getName(), m.getAddress(), m.getLongitude(), m.getLatitude(), m.getStarAvg(), m.getReviewCnt()))
                .collect(Collectors.toList());

        return ApiResponse.success("data", new Result(collect.size(), collect));

    }


    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T placeInfo;
    }


    //카테고리 선택
    @GetMapping("/offline-map/category")
    public ApiResponse getPlaceInfoByCategory(@RequestParam(value = "category")String category) {
        List<placeInfo> list = apiRepository.findAllByCategory(category);

        //entity -> dto 변환
        List<OfflineMapDto.placeDto> collect = list.stream().map(m -> new OfflineMapDto.placeDto(m.getId(), m.getCategory(), m.getName(),
                        m.getAddress(), m.getLongitude(), m.getLatitude(), m.getStarAvg(), m.getReviewCnt()))
                .collect(Collectors.toList());

        //return collect;

        return ApiResponse.success("data", collect);
    }


    //마커 선택(가게 정보)
    @GetMapping("/offline-map/{placeid}/marker")
    public ApiResponse getPlaceInfoByMarker(@PathVariable("placeid") Long placeid) {
        placeInfo placeInfo = apiRepository.findById(placeid).get();
        OfflineMapDto.CategoryDto categoryDto = new OfflineMapDto.CategoryDto(placeInfo.getId(), placeInfo.getName(), placeInfo.getCategory(),
                placeInfo.getAddress(), placeInfo.getStarAvg(), placeInfo.getReviewCnt());

        return ApiResponse.success("data", categoryDto);
    }



    /**
     * 검색 API
     */
    @GetMapping("/offline-map/search")
    public ApiResponse search(@RequestParam(value = "keyword") String keyword) {
        List<placeInfo> list = apiRepository.findByKeyword(keyword);
        return ApiResponse.success("data", list);
    }

}
