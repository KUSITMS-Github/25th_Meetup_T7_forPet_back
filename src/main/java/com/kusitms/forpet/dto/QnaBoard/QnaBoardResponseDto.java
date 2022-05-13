package com.kusitms.forpet.dto.QnaBoard;

import com.kusitms.forpet.domain.CommentQna;
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
    private int comments;   //댓글 수
    private String[] imageUrlList;

}
