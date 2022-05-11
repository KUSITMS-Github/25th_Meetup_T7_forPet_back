package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkByUserIdDto {
    private Long bookmarkId;
    private Long userId;

    private Long placeId;
    private String category;
    private String name;
    private String address;
    private String longitude;
    private String latitude;
}
