package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.CommentQna;
import com.kusitms.forpet.dto.MyPage.BookmarkOfflineDto;
import com.kusitms.forpet.dto.MyPage.HistoryBoardDTO;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.MyPageService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final TokenProvider tokenProvider;


    /**
     * 오프라인 지도 빼고 community 수정해야함!
     *
     */



    //마이페이지 히스토리 내가 쓴 글(커뮤니티, 백과사전)
    @GetMapping("/mypage/history/board")
    public MyPageService.Result getBoard(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return myPageService.getBoard(userid);
    }


    //마이페이지 히스토리 답글(커뮤니티, 백과사전)
    @GetMapping("/mypage/history/comment")
    public List<HistoryBoardDTO> getBoardByComment(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return myPageService.getBoardByComment(userid);
    }



    //마이페이지
    @GetMapping("/mypage/bookmark/offlineMap")
    public List<BookmarkOfflineDto> getBookmarkOfflineMap(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return myPageService.getBookmarkOfflineMap(userid);
    }


    @GetMapping("/mypage/bookmark/board")
    public List<HistoryBoardDTO> getBookmarkBoard(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return myPageService.getBookmarkBoard(userid);

    }


}
