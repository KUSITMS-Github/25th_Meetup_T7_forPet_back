package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String nickName;
    private String profileImageUrl;
    private double star;
    private String content;
    private String createDate;
    private String[] imageUrlList;

}

