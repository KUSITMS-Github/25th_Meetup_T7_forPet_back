package com.kusitms.forpet.controller;

import com.kusitms.forpet.dto.CommentQnaRespDto;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.CommentQnaService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentQnaController {

    private final CommentQnaService commentQnaService;
    private final TokenProvider tokenProvider;


    //댓글 생성
    @PostMapping("/qnaBoard/{boardId}/comment")
    public Long commentSave(HttpServletRequest request,
                            @PathVariable("boardId") Long boardId,
                            @RequestParam String comment) {

        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return commentQnaService.commentSave(userid, boardId, comment);
    }


    //백과사전 게시글 댓글 전체 조회
    @GetMapping("/qnaBoard/{boardId}/comment")
    public Result getCommentList(@PathVariable("boardId") Long boardId) {
        List<CommentQnaRespDto> commentList = commentQnaService.getCommentList(boardId);

        return new Result(boardId, commentList);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private Long boardId;
        private T data;
    }

    //백과사전 댓글 좋아요
    @PostMapping("/qnaBoard/comment/{commentId}/like")
    public int CommentQnaLikes(@PathVariable(value = "commentId") Long commentId) {
        return commentQnaService.saveLikes(commentId);
    }
}
