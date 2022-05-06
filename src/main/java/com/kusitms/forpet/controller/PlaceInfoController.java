package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.dto.CategoryDto;
import com.kusitms.forpet.dto.placeDto;
import com.kusitms.forpet.repository.APIRepository;
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
    private final APIRepository apiRepository;

    @GetMapping("/offline-map")
    public Result getApi() {
        List<placeInfo> list = apiService.findAll();
        //entity -> dto 변환
        List<placeDto> collect = list.stream().map(m -> new placeDto(m.getId(), m.getCategory(), m.getName(), m.getAddress(), m.getLongitude(), m.getLatitude()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);

    }

    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    //카테고리 선택
    @GetMapping("/offline-map/category")
    public List<CategoryDto> getPlaceInfoByCategory(@RequestParam(value = "category")String category) {
        List<placeInfo> list = apiRepository.findAllByCategory(category);

        //entity -> dto 변환
        List<CategoryDto> collect = list.stream().map(m -> new CategoryDto(m.getId(), m.getName(), m.getCategory(), m.getAddress(), m.getStarAvg(), m.getReviewCnt(), m.getBookMarkList().size()))
                .collect(Collectors.toList());

        return collect;

    }

    //마커 선택(가게 정보)
    @GetMapping("/offline-map/{placeid}/marker")
    public CategoryDto getPlaceInfoByMarker(@PathVariable("placeid") Long placeid) {
        placeInfo placeInfo = apiRepository.findById(placeid).get();
        CategoryDto categoryDto = new CategoryDto(placeInfo.getId(), placeInfo.getName(), placeInfo.getCategory(),
                placeInfo.getAddress(), placeInfo.getStarAvg(), placeInfo.getReviewCnt(), placeInfo.getBookMarkList().size());

        return categoryDto;
    }

    /**
     * 검색 API
     */
    @GetMapping("/offline-map/serach")
    public List<placeInfo> search(@RequestParam(value = "keyword") String keyword) {
        return apiRepository.findByKeyword(keyword);
    }

}
