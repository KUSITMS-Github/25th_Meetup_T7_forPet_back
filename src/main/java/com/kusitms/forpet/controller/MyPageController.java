package com.kusitms.forpet.controller;

import com.kusitms.forpet.dto.response.ApiResponse;
import com.kusitms.forpet.dto.MyPageDto;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.MyPageService;
import com.kusitms.forpet.service.UserService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final UserService userService;
    private final MyPageService myPageService;
    private final TokenProvider tokenProvider;

    /**
     * 마이페이지 : 내 정보 조회
     * 닉네임, 실명, 프로필 사진, 인증여부, 동네
     */
    @GetMapping("")
    public ApiResponse getMyProfile(HttpServletRequest request) {
        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userId = tokenProvider.getUserIdFromToken(accessToken);

        MyPageDto.UserDetailDto dto = myPageService.getUser(userId);

        return ApiResponse.success("data", dto);
    }

    /**
     * 마이페이지 : 타인 정보 조회
     * 프로필 사진, 닉네임, 동네
     */
    @GetMapping("/{id}")
    public ApiResponse getOthersProfile(@PathVariable(value = "id") Long id) {
        MyPageDto.UserDetailDto dto = myPageService.getUser(id);

        return ApiResponse.success("data", dto);
    }
    /**
     * 마이페이지 회원 정보 수정
     * 프로필 사진, 닉네임
     */
    @PutMapping("/edit")
    public ApiResponse updateMyProfile(HttpServletRequest request,
                                       @RequestPart(value ="user_update_dto") MyPageDto.UserUpdateDto dto,
                                       @RequestPart(value = "profile_image", required=false) MultipartFile profileImage) {
        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userId = tokenProvider.getUserIdFromToken(accessToken);

        MyPageDto.UserDetailDto result = myPageService.updateUser(userId, dto, profileImage);

        return ApiResponse.success("data", result);
    }
    /**
     * 오프라인 지도 빼고 community 수정해야함!
     *
     */


    /**
     * 마이페이지 히스토리 내가 쓴 글(커뮤니티, 백과사전)
     */
    @GetMapping("/history/board")
    public ApiResponse getBoard(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        MyPageService.Result result = myPageService.getBoard(userid);

        return ApiResponse.success("data", result);
    }


    /**
     * 마이페이지 히스토리 답글(커뮤니티, 백과사전)
     */
    @GetMapping("/history/comment")
    public List<MyPageDto.HistoryBoardDTO> getBoardByComment(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return myPageService.getBoardByComment(userid);
    }


    //마이페이지
    @GetMapping("/bookmark/offlineMap")
    public List<MyPageDto.BookmarkOfflineDto> getBookmarkOfflineMap(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        return myPageService.getBookmarkOfflineMap(userid);
    }


    @GetMapping("/bookmark/board")
    public ApiResponse getBookmarkBoard(HttpServletRequest request) {

        //userid 값 가져오기
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userid = tokenProvider.getUserIdFromToken(accessToken);

        MyPageService.Result result = myPageService.getBookmarkBoard(userid);

        return ApiResponse.success("data", result);
    }


}
