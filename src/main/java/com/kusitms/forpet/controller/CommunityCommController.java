package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.Community;
import com.kusitms.forpet.dto.CommunityDto;
import com.kusitms.forpet.dto.QnaBoardDto;
import com.kusitms.forpet.dto.response.ApiResponse;
import com.kusitms.forpet.repository.CommunityRep;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.CommentCommService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityCommController {

    private final CommentCommService commentCommService;
    private final CommunityRep communityRep;
    private final TokenProvider tokenProvider;

    // 댓글 생성
    @PostMapping("/community/{postId}/comment")
    public Long saveComment(HttpServletRequest request,
                            @PathVariable("postId") Long postId,
                            @RequestBody QnaBoardDto.CommentQnaReqDto comment){

        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return commentCommService.commentSave(userid, postId, comment.getComment());
    }

    // 댓글 조회
    @GetMapping("/community/{postId}/comment")
    public ApiResponse getCommentList(@PathVariable("postId") Long postId) {
        Community community = communityRep.findById(postId).get();
        int size = community.getCommentCommList().size();

        List<CommunityDto.CommentParentResDto> list = commentCommService.getCommentList(postId);

        return ApiResponse.success("data", new Result(postId, size,list));
    }

    // 리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private Long post_id;
        private int commentCount;
        private T comments;
    }

}
