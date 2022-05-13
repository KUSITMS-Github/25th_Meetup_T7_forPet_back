package com.kusitms.forpet.dto.QnaBoard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentQnaRespDto {
    private Long Id;                //댓글 id
    private String imageUrl;        //작성자 프로필 사진 Url
    private String nickName;        //작성자 닉네임
    private String tag;           //반려인, 예비반려인 태그
    private String comment;         //댓글 내용
    private LocalDateTime createDate; //댓글 작성일
    private int likes;              //댓글 좋아요 수
}
