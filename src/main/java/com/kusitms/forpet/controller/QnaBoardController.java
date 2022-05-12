package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.dto.QnaBoardRequestDto;
import com.kusitms.forpet.dto.QnaBoardResponseDto;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.QnaBoardService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;
    private final QnaBoardRep qnaBoardRep;
    private final TokenProvider tokenProvider;


    //백과사전 글 생성
    @PostMapping("/qnaBoard")
    public Long createQnaBoard(HttpServletRequest request,
                               @RequestPart(value = "qnaBoardRequestDto")QnaBoardRequestDto qnaBoardRequestDto,
                               @RequestPart(value = "imageList") List<MultipartFile> multipartFiles) {
        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return qnaBoardService.createQnaBoard(userid, qnaBoardRequestDto.getTitle(), qnaBoardRequestDto.getContent(),
                qnaBoardRequestDto.getHashTag(), multipartFiles);

    }



    //백과사전 글 리스트 최신순 조회(페이징)
    @GetMapping("/qnaBoard/orderByLatest")
    public Result getQnaBoardByLatest(@PageableDefault(size = 3, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<QnaBoard> postList = qnaBoardRep.findAll(pageable);


        //entity -> dto 변환
        List<QnaBoardResponseDto> collect = postList.stream().map(m -> new QnaBoardResponseDto(m.getId(),
                        //m.getUser().getTag,
                        m.getUser().getNickname(),
                        m.getTitle(), m.getContent(), m.getCreateDate(),
                        m.getLikes(), m.getBookmarkQnaList().size(), m.getCommentQnaList().size(),
                        m.getImageUrlList().split("#")))
                .collect(Collectors.toList());

        return new Result(postList.getNumber(), postList.getNumberOfElements(), postList.getTotalPages(), postList.getTotalElements(), collect);

    }


    //백과사전 글 리스트 추천순 조회(페이징)
    @GetMapping("/qnaBoard/orderByLikes")
    public Result getQnaBoardByLikes(@PageableDefault(size = 3, sort = "likes", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<QnaBoard> postList = qnaBoardRep.findAll(pageable);


        //entity -> dto 변환
        List<QnaBoardResponseDto> collect = postList.stream().map(m -> new QnaBoardResponseDto(m.getId(),
                        //m.getUser().getTag,
                        m.getUser().getNickname(),
                        m.getTitle(), m.getContent(), m.getCreateDate(),
                        m.getLikes(), m.getBookmarkQnaList().size(), m.getCommentQnaList().size(),
                        m.getImageUrlList().split("#")))
                .collect(Collectors.toList());

        return new Result(postList.getNumber(), postList.getNumberOfElements(), postList.getTotalPages(), postList.getTotalElements(), collect);

    }


    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int number;             //페이지 번호
        private int numberOfElement;    //페이지 요소 개수
        private int totalPages;         //전체 페이지 개수
        private Long totalElements;     //잔체 요소 개수
        private T data;
    }



    //백과사전 좋아요
    @PostMapping("/qnaBoard/{boardId}/like")
    public int QnaBoardLikes(@PathVariable(value = "boardId") Long boardId) {
        return qnaBoardService.saveLikes(boardId);
    }


    //백과사전 북마크 생성
    @PostMapping("/qnaBoard/{boardId}/bookmark")
    public int QnaBoardBookmark(HttpServletRequest request,
                                @PathVariable(value = "boardId") Long boardId) {
        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return qnaBoardService.createBookmark(userid, boardId);
    }

}
