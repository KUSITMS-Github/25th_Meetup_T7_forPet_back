package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkByCategoryDto {
    private Long bookmarkId;
    private Long placeId;
    private String placeName;
    private String placeAddress;
    private String longitude;     //경도
    private String latitude;    //위도
}