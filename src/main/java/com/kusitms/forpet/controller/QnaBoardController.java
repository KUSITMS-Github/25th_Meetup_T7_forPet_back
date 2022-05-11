package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.dto.QnaBoardRequestDto;
import com.kusitms.forpet.dto.QnaBoardResponseDto;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.QnaBoardService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;
    private final QnaBoardRep qnaBoardRepository;
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


    /**
     * 최신순, 추천순 추가하기
    //백과사전 글 리스트 조회
    @GetMapping("/qnaBoard")
    public List<QnaBoardResponseDto> getQnaBoard() {
        List<QnaBoard> list = qnaBoardRepository.findAll();

        //entity -> dto 변환
        List<QnaBoardResponseDto> collect = list.stream().map(m -> new QnaBoardResponseDto(m.getId(),
                        //m.getUser().getTag,
                        m.getUser().getNickname(),
                        m.getTitle(), m.getContent(), m.getCreateDate(), m.getLikes(), m.getBookmarkQnaList().size(), m.getImageUrlList().split("#")))
                .collect(Collectors.toList());

        return collect;
    }
    */

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
