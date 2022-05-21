package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.PetCard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.UserDto;
import com.kusitms.forpet.dto.response.ApiResponse;
import com.kusitms.forpet.dto.response.ErrorCode;
import com.kusitms.forpet.exception.CustomException;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.JWTTokenService;
import com.kusitms.forpet.service.JoinService;
import com.kusitms.forpet.service.PetCardService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class JoinController {
    private final JoinService joinService;
    private final PetCardService petCardService;
    private final TokenProvider tokenProvider;
    private final JWTTokenService jwtTokenService;

    /*
        카카오 회원 정보 반환
     */
    @GetMapping("")
    public ApiResponse getUserInfo(HttpServletRequest request) {
        String accessToken = HeaderUtil.getAccessToken(request);

        // 회원가입 시 가져오는 token은 유효하지 않을 수 있음.
        Long userId = tokenProvider.getUserIdFromExpiredToken(accessToken);

        User kakaoUser = joinService.findByUserId(userId);

        UserDto.KakaoUserDto userDto = new UserDto.KakaoUserDto(kakaoUser.getUserId(), kakaoUser.getName(), kakaoUser.getEmail(), kakaoUser.getImageUrl());

        return ApiResponse.success("data", userDto);
    }

    /*
    닉네임 중복 체크
     */
    @GetMapping("/check/nickname")
    public ApiResponse checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = joinService.findByNickname(nickname);
        return ApiResponse.success("isDuplicate", isDuplicate);
    }

    /*
    휴대전화 인증
    */
    @GetMapping("/check/sendSMS")
    public String sendSMS(@RequestParam(name="phone_number")String phoneNumber) {
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 4; i++) {
            numStr += Integer.toString(rand.nextInt(10));
        }

        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);
        joinService.certifiedPhoneNumber(phoneNumber, numStr);
        return numStr;
    }

    /*
        동물 인증 카드 (추후 특정 로직 구현 필요)
     */
    @PostMapping("/check/pet-card")
    public ApiResponse checkPetCard(@RequestPart(value="pet_card_image") MultipartFile petCardImage) {
        if(petCardImage.isEmpty()) {
            throw new CustomException(ErrorCode.CANNOT_CERTIFY);
        }
        return ApiResponse.success("data", null);
    }

    /*
    회원가입
    */
    @PostMapping("")
    public ApiResponse signup(@RequestPart(value ="signup_dto") UserDto.SignUpDto dto,
                              @RequestPart(value = "profile_image", required=false) MultipartFile profileImage,
                              @RequestPart(value = "pet_card_image", required=false) MultipartFile petCardImage,
                              HttpServletRequest request, HttpServletResponse response) {

        String accessToken = HeaderUtil.getAccessToken(request);

        // 회원가입을 진행하며 token이 만료되었을 수 있다.
        Long userId = tokenProvider.getUserIdFromExpiredToken(accessToken);

        User user = joinService.createUser(userId, dto, profileImage);

        // access token과 refresh token 발급
        accessToken = jwtTokenService.createJWTToken(user, request, response);

        if(!petCardImage.getOriginalFilename().equals("")) {
            PetCard petCard = petCardService.createPetCardByUserId(userId, petCardImage);
        }

        return ApiResponse.created("token", accessToken);
    }
}
