package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class QnaBoardResponseDto {
    private Long qnaBoardId;
    //private String tag;
    private String nickName;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private int likes;      //좋아요 수
    private int bookmark;   //북마크 수
    private String[] imageUrlList;

    //좋아요 개수
    //스크랩 개수
    //댓글 개수
}
