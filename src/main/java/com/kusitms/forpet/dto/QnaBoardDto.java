package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QnaBoardDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentQnaReqDto {
        public String comment;
    }


    @Data
    @AllArgsConstructor
    public static class CommentQnaRespDto {
        private Long Id;                //댓글 id
        private String imageUrl;        //작성자 프로필 사진 Url
        private String nickName;        //작성자 닉네임
        private String tag;           //반려인, 예비반려인 태그
        private String comment;         //댓글 내용
        private String createDate; //댓글 작성일
        private int likes;              //댓글 좋아요 수
    }


    @Data
    @AllArgsConstructor
    public static class QnaBoardRequestDto {
        public String title;
        public String content;
    }


    @Data
    @AllArgsConstructor
    public static class QnaBoardResByIdDto {
        private Long qnaBoardId;
        private String profileImage;
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



    @Data
    @AllArgsConstructor
    public static class QnaBoardResponseDto {
        private Long qnaBoardId;
        private String tag;
        private String nickName;
        private String profileImage;
        private String title;
        private String content;
        private String createDate;
        private int likes;      //좋아요 수
        private int bookmark;   //북마크 수
        private int comments;   //댓글 수
        private String[] imageUrlList;

    }
}
