package com.kusitms.forpet.dto;

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
     * 게시글 response dto
     */
    @Data
    @AllArgsConstructor
    public static class CommunityResponse {
        private Long postId;
        private Long userId;
        private String title;
        private String content;
        private LocalDateTime date;
        private Long thumbsUpCnt;
        private String imageUrlList;
        private String category;
    }
}
