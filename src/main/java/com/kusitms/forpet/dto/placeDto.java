package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class placeDto {
    private Long id;
    private String category;    //카테고리
    private String name;        //병원명
    private String address;     //소재지 주소(도로명)
    private String longitude;   //경도
    private String latitude;    //위도
    private double star;        //별점수
    private int reviewCnt;      //리뷰 개수
}
