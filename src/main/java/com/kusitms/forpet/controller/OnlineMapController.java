package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.MapClickCount;
import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.dto.ClickDto;
import com.kusitms.forpet.dto.placeDto;
import com.kusitms.forpet.service.OnlineMapService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/online-map")
public class OnlineMapController {
    private final OnlineMapService onlineMapService;

    @GetMapping("")
    public Result getAllClick() {
        List<MapClickCount> list = onlineMapService.getAllClickCnt();
        //entity -> dto 변환
        List<ClickDto> collect = list.stream().map(m -> new ClickDto(m.getId(), m.getClickCnt()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @PostMapping("/{id}")
    public ApiResponse updateClickCnt(@PathVariable Long id) {
        long clickCnt = onlineMapService.click(id);
        return ApiResponse.success("clickCnt", clickCnt);
    }

    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }
}

