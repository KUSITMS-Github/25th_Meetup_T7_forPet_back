package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.dto.placeDto;
import com.kusitms.forpet.service.APIService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class APIController {
    private final APIService apiService;

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
}
