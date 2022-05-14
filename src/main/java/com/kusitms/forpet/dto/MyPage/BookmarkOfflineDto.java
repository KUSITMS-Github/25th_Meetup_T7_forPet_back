package com.kusitms.forpet.dto.MyPage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkOfflineDto {
    private Long id;             //장소 id
    private String category;    //카테고리
    private String name;        //이름
    private String address;     //도로명 주소
    private double starAvg;     //별점 평균
    private int reviewCnt;   //리뷰 수
}
