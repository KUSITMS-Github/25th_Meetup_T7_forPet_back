package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;        //병원명
    private String category;
    private String address;    //소재지 주소(도로명)
    private double starAvg;    //별점 평균
    private int reviewCnt;  //리뷰 수
}
