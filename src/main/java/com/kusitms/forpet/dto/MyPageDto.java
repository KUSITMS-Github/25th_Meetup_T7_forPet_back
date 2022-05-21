package com.kusitms.forpet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageDto {

    @Data
    @AllArgsConstructor
    public static class BookmarkOfflineDto {
        private Long id;             //장소 id
        private String category;    //카테고리
        private String name;        //이름
        private String address;     //도로명 주소
        private double starAvg;     //별점 평균
        private int reviewCnt;   //리뷰 수
    }



    @Data
    @AllArgsConstructor
    public static class HistoryBoardDTO {
        private Long id;
        private String category;
        private String title;
    }


    @Data
    @AllArgsConstructor
    public static class UserDetailDto {
        private Long id;
        @JsonProperty(value = "is_certified_address")
        private boolean isCertifiedAddress;
        @JsonProperty(value = "is_certified_pet_card")
        private boolean isCertifiedPetCard;
        private String[] address;
        private String nickname;
        private String name;
        @JsonProperty(value = "profile_image_url")
        private String profileImageUrl;
    }



    @Data
    @AllArgsConstructor
    public static class UserUpdateDto {
        private String nickname;
        private String address;
    }


}
