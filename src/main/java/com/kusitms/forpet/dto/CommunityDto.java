package com.kusitms.forpet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CommunityDto {

    /**
     * 게시글 request dto
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityRequest {
        private String title;
        private String content;
        private String category;
    }

    /**
     * 게시글 메인 페이지 response dto
     */
    @Data
    @AllArgsConstructor
    public static class CommunityResponse {
        @JsonProperty(value = "post_id")
        private Long postId;
        @JsonProperty(value = "user_id")
        private Long userId;
        private String title;
        private int likes;
        private int bookmarks;
        @JsonProperty(value = "image_url_list")
        private String[] imageUrlList;
        private String category;
        @JsonProperty(value = "comment_cnt")
        private int commentCnt;
    }

    /**
     * 게시글 카테고리, 검색 리스트 response dto
     */
    @Data
    @AllArgsConstructor
    public static class CommunityListResponse {
        @JsonProperty(value = "post_id")
        private Long postId;
        private Writer writer;
        private String title;
        private int likes;
        private int bookmarks;
        @JsonProperty(value = "image_url_list")
        private String[] imageUrlList;
        private String category;
        @JsonProperty(value = "comment_cnt")
        private int commentCnt;
        private LocalDateTime createdDate;
    }

    /**
     * 게시글 상세 조회 response dto
     */
    @Data
    @AllArgsConstructor
    public static class CommunityDetailResponse {
        @JsonProperty(value = "post_id")
        private Long postId;
        private Writer writer;
        @JsonProperty(value = "is_writer")
        private Boolean isWriter;
        private String title;
        private String content;
        private LocalDateTime date;
        private int likes;
        private int bookmarks;
        @JsonProperty(value = "image_url_list")
        private String[] imageUrlList;
        private String category;
        @JsonProperty(value = "comment_cnt")
        private int commentCnt;
        @JsonProperty(value = "is_like")
        private boolean isLike;
        @JsonProperty(value = "is_bookmark")
        private boolean isBookMark;
    }

    @Data
    @AllArgsConstructor
    public static class Writer {
        @JsonProperty(value = "user_id")
        private Long userId;
        @JsonProperty(value = "user_profile_image")
        private String userProfileImage;
        @JsonProperty(value="user_nickname")
        private String userNickname;
    }

    /**
     * 부모 댓글
     */
    @Data
    @AllArgsConstructor
    public static class CommentParentResDto {
        private Long parentId;      // 부모 댓글만 개발하니깐 null값 반환
        private Long commentId;     // 댓글 고유 id
        private String content;
        private String username;
        private Long userId;
        private String profileImage;
        private String createDate;
        private List<CommentChildResDto> children;
    }



    /**
     * 자식 댓글
     */
    @Data
    @AllArgsConstructor
    public static class CommentChildResDto {
        private Long id;
        private Long parentId;
        private String content;
    }
}
