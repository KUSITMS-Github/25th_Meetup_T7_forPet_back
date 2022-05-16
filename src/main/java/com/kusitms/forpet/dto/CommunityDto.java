package com.kusitms.forpet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
        @JsonProperty(value = "thumbs_up_cnt")
        private Long thumbsUpCnt;
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
        @JsonProperty(value = "thumbs_up_cnt")
        private Long thumbsUpCnt;
        @JsonProperty(value = "image_url_list")
        private String[] imageUrlList;
        private String category;
        @JsonProperty(value = "comment_cnt")
        private int commentCnt;
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
        @JsonProperty(value = "thumbs_up_cnt")
        private Long thumbsUpCnt;
        @JsonProperty(value = "image_url_list")
        private String[] imageUrlList;
        private String category;
        @JsonProperty(value = "comment_cnt")
        private int commentCnt;
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
}
