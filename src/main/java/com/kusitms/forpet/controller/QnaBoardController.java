package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.BookmarkQna;
import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.dto.QnaBoard.QnaBoardRequestDto;
import com.kusitms.forpet.dto.QnaBoard.QnaBoardResByIdDto;
import com.kusitms.forpet.dto.QnaBoard.QnaBoardResponseDto;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.security.Role;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.QnaBoardService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;
    private final QnaBoardRep qnaBoardRep;
    private final TokenProvider tokenProvider;


    //백과사전 글 생성
    @PostMapping("/qnaBoard")
    public ApiResponse createQnaBoard(HttpServletRequest request,
                                      @RequestPart(value = "qnaBoardRequestDto")QnaBoardRequestDto qnaBoardRequestDto,
                                      @RequestPart(value = "imageList", required = false) List<MultipartFile> multipartFiles) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        Long id = qnaBoardService.createQnaBoard(userid, qnaBoardRequestDto.getTitle(), qnaBoardRequestDto.getContent(), multipartFiles);

        return ApiResponse.success("data", id);
    }


    //백과사전 게시글
    @GetMapping("/qnaBoard/{boardId}")
    public ApiResponse getBoardWithComment(HttpServletRequest request, @PathVariable(value = "boardId") Long boardId) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        QnaBoardResByIdDto dto = qnaBoardService.getBoardById(userid, boardId);

        return ApiResponse.success("data", dto);
    }


    //백과사전 글 리스트 최신순 조회(페이징)
    @GetMapping("/qnaBoard/orderByLatest")
    public ApiResponse getQnaBoardByLatest(@PageableDefault(size = 3, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<QnaBoard> list = qnaBoardRep.findAll(pageable);

        List<QnaBoardResponseDto> collect = new ArrayList<>();

        for(QnaBoard q : list) {
            if(q.getUser().getRole().equals(Role.FORPET_USER)) {
                if(q.getImageUrlList() != null) {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            q.getImageUrlList().split("#")));
                }
                else {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            null));
                }

            }

            if(q.getUser().getRole().equals(Role.USER)) {
                if(q.getImageUrlList() != null) {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "예비반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            q.getImageUrlList().split("#")));
                } else {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "예비반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            null));
                }

            }
        }

        
        return ApiResponse.success("data", new Result(list.getNumber(), list.getNumberOfElements(), list.getTotalPages(), list.getTotalElements(), collect));

    }



    //백과사전 글 리스트 추천순 조회(페이징)
    @GetMapping("/qnaBoard/orderByLikes")
    public ApiResponse getQnaBoardByLikes(@PageableDefault(size = 3, sort = "likes", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<QnaBoard> list = qnaBoardRep.findAll(pageable);

        List<QnaBoardResponseDto> collect = new ArrayList<>();

        for(QnaBoard q : list) {
            if(q.getUser().getRole().equals(Role.FORPET_USER)) {
                if(q.getImageUrlList() != null) {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            q.getImageUrlList().split("#")));
                }
                else {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            null));
                }

            }

            if(q.getUser().getRole().equals(Role.USER)) {
                if(q.getImageUrlList() != null) {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "예비반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            q.getImageUrlList().split("#")));
                } else {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "예비반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            null));
                }

            }
        }
        
        return ApiResponse.success("data", new Result(list.getNumber(), list.getNumberOfElements(), list.getTotalPages(), list.getTotalElements(), collect));

    }



    //백과사전 글 리스트 검색 조회(페이징)
    @GetMapping("/qnaBoard/search")
    public ApiResponse search(@RequestParam(value = "keyword") String keyword,
                         @RequestParam(value = "orderBy") String orderBy,
                         @RequestParam(value = "page") int page,
                         Pageable pageable) {

        Page<QnaBoard> list = null;

        if(orderBy.equals("latest")) {
            pageable = PageRequest.of(page, 3, Sort.by("create_date").descending());
            list = qnaBoardRep.findAllSearch(keyword, pageable);
        }
        else {
            pageable = PageRequest.of(page, 3, Sort.by("likes").descending());
            list = qnaBoardRep.findAllSearch(keyword, pageable);
        }


        List<QnaBoardResponseDto> collect = new ArrayList<>();

        for(QnaBoard q : list) {
            if(q.getUser().getRole().equals(Role.FORPET_USER)) {
                if(q.getImageUrlList() != null) {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            q.getImageUrlList().split("#")));
                }
                else {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            null));
                }

            }

            if(q.getUser().getRole().equals(Role.USER)) {
                if(q.getImageUrlList() != null) {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "예비반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            q.getImageUrlList().split("#")));
                } else {
                    collect.add(new QnaBoardResponseDto(q.getId(),
                            "예비반려인",
                            q.getUser().getNickname(),
                            q.getTitle(), q.getContent(), q.getCreateDate(),
                            q.getLikes(), q.getBookmarkQnaList().size(), q.getCommentQnaList().size(),
                            null));
                }

            }
        }
        
        return ApiResponse.success("data", new Result(list.getNumber(), list.getNumberOfElements(), list.getTotalPages(), list.getTotalElements(), collect));
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
    public ApiResponse QnaBoardLikes(@PathVariable(value = "boardId") Long boardId) {
        int cnt = qnaBoardService.saveLikes(boardId);
        return ApiResponse.success("data", cnt);
    }


    //백과사전 좋아요 취소
    @PutMapping("/qnaBoard/{boardId}/like")
    public ApiResponse deleteQnaBoardLikes(@PathVariable(value = "boardId") Long boardId) {
        int cnt = qnaBoardService.deleteLikes(boardId);
        return ApiResponse.success("data", cnt);
    }


    //백과사전 북마크 생성
    @PostMapping("/qnaBoard/{boardId}/bookmark")
    public ApiResponse QnaBoardBookmark(HttpServletRequest request,
                                               @PathVariable(value = "boardId") Long boardId) {
        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        Map<String, Integer> result = qnaBoardService.createBookmark(userid, boardId);

        if(result != null)
            return ApiResponse.success("data", result);
        else
            return ApiResponse.badRequest();

    }


    //백과사전 북마크 취소
    @DeleteMapping("/qnaBoard/{boardId}/bookmark")
    public ApiResponse deleteBookmark(HttpServletRequest request, @PathVariable(value = "boardId") Long boardId) {
        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        int cnt = qnaBoardService.deleteBookmark(userid, boardId);

        return ApiResponse.success("data", cnt);
    }


}
