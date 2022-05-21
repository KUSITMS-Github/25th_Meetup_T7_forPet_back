package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OfflineMapDto {

    @Data
    @AllArgsConstructor
    public static class CategoryDto {
        private Long id;
        private String name;        //병원명
        private String category;
        private String address;    //소재지 주소(도로명)
        private double starAvg;    //별점 평균
        private int reviewCnt;  //리뷰 수
    }


    @Data
    @AllArgsConstructor
    public static class placeDto {
        private Long id;
        private String category;
        private String name;        //병원명
        private String address;    //소재지 주소(도로명)
        private String longitude;     //경도
        private String latitude;    //위도
    }


    @Data
    @AllArgsConstructor
    public static class ReviewDto {
        private Long id;
        private String nickName;
        private String profileImageUrl;
        private int star;
        private String content;
        private String createDate;
        private String[] imageUrlList;

    }



    @Data
    @AllArgsConstructor
    public static class ReviewRequestDto {
        private int star;
        private String content;
    }


    /**
     * 북마크 (카테고리)
     */
    @Data
    @AllArgsConstructor
    public static class BookmarkByCategoryDto {
        private Long bookmarkId;
        private Long placeId;
        private String placeName;
        private String placeAddress;
        private String longitude;     //경도
        private String latitude;    //위도
    }


    /**
     * 북마크 (회원)
     */
    @Data
    @AllArgsConstructor
    public static class BookmarkByUserIdDto {
        private Long bookmarkId;
        private Long userId;

        private Long placeId;
        private String category;
        private String name;
        private String address;
        private String longitude;
        private String latitude;
    }

}
