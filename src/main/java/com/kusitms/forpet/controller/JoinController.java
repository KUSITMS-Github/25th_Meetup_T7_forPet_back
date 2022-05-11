package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.PetCard;
import com.kusitms.forpet.domain.Terms;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.*;
import com.kusitms.forpet.service.JoinService;
import com.kusitms.forpet.service.PetCardService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class JoinController {
    private final JoinService joinService;
    private final PetCardService petCardService;

    @GetMapping("/{id}/terms")
    public Result getTerms(@PathVariable String id) {
        List<Terms> TermsList = joinService.findAll();
        List<TermsDto> collect = TermsList.stream().map(m -> new TermsDto(m.getTermsId(), m.getName(), m.getContent(), m.getIs_required()))
                .collect(Collectors.toList());
        return new Result(id, collect);
    }

    @PostMapping("/terms")
    public ApiResponse setTerms(@RequestBody TermsRecordDto dto) {
        Long userId = joinService.saveTermsRecord(dto);
        return ApiResponse.created("userId", userId);
    }

    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private String id;
        private T data;
    }

    /*
        카카오 회원 정보 반환
     */
    @GetMapping("/{id}")
    public ApiResponse getUserInfo(@PathVariable String id) {
        Long userId = Long.valueOf(id);
        User kakaoUser = joinService.findByUserId(userId);

        KakaoUserDto userDto = new KakaoUserDto(kakaoUser.getUserId(), kakaoUser.getName(), kakaoUser.getEmail(), kakaoUser.getImageUrl());
        return ApiResponse.success("user", userDto);
    }

    /*
    닉네임 중복 체크
     */
    @GetMapping("/check/nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = joinService.findByNickname(nickname);
        return ResponseEntity.ok(isDuplicate);
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
    회원가입
    */
    @PostMapping("/{id}")
    public ApiResponse signup(@PathVariable Long id,
                              @RequestPart(value ="signup_dto") SignUpDto dto,
                              @RequestPart(value = "profile_image", required=false) MultipartFile profileImage,
                              @RequestPart(value = "pet_card_image", required=false) MultipartFile petCardImage) {

        User user = joinService.createUser(id, dto, profileImage);
        UserDto userDto = new UserDto(user.getUserId(), user.getName(), user.getEmail(), user.getImageUrl(), user.getNickname(), user.getPhone(), null,
                new SignUpDto.Address(user.getAddress1(), user.getAddress2(), user.getAddress3()), null, user.getCustomImageUrl());
        if(!petCardImage.getOriginalFilename().equals("")) {
            PetCard petCard = petCardService.createPerCardByUserId(id, petCardImage, dto.getPetCardNumber());
            userDto.setPetCardNumber(petCard.getCardNumber());
            userDto.setPetCardImageUrl(petCard.getImageUrl());
        }
        return ApiResponse.created("user", dto);
    }
}
