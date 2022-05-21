package com.kusitms.forpet.controller;

import com.kusitms.forpet.dto.Community.CommentParentResDto;
import com.kusitms.forpet.dto.QnaBoard.CommentQnaReqDto;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.CommentCommServ;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CommentCommCont {

    private final CommentCommServ commentCommServ;
    private final TokenProvider tokenProvider;


    // 부모 댓글 생성
    @PostMapping("/community/comment/{postId}")
    public Long saveComment(HttpServletRequest request,
                                   @PathVariable("postId") Long postId,
                                   @RequestBody CommentQnaReqDto comment) {
        // userId 값 뺴오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return commentCommServ.saveParentComment(userid, postId, comment.getComment());     // 부모 댓글 생성

    }


    //자식 댓글 생성
    @PostMapping("/community/comment/{postId}/{parentCommentId}")
    public Long saveChildComment(HttpServletRequest request,
                                 @PathVariable("postId") Long postId,
                                 @PathVariable(value = "parentCommentId", required = false) Long parentCommentId,
                                 @RequestBody CommentQnaReqDto comment) {
        // userId 값 뺴오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return commentCommServ.saveChildComment(userid, postId, parentCommentId, comment.getComment());    //자식 댓글 생성
    }


    //댓글 전체 조회
    @GetMapping("/community/{postId}/comment")
    public List<CommentParentResDto> getComments(@PathVariable("postId") Long postId) {
        return commentCommServ.getComments(postId);
    }

}
