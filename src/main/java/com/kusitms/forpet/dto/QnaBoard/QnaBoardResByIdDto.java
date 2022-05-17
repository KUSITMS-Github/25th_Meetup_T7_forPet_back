package com.kusitms.forpet.dto.QnaBoard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QnaBoardResByIdDto {
    private Long qnaBoardId;
    private boolean toggle;  //접속한 회원의 게시글 북마크 여부
    private String tag;
    private String nickName;
    private String title;
    private String content;
    private String createDate;
    private int likes;      //좋아요 수
    private int bookmark;   //북마크 수
    private int comments;   //댓글 수
    private String[] imageUrlList;
}
