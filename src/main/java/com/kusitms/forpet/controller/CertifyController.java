package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.PetCard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.dto.SignUpDto;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.PetCardService;
import com.kusitms.forpet.service.UserService;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certify")
public class CertifyController {
    private final PetCardService petCardService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    /**
     *  동네, 동물 카드 등록되었는지 여부
     */
    @GetMapping("")
    public ApiResponse certify(HttpServletRequest request) {
        String accessToken = HeaderUtil.getAccessToken(request);

        Long userId = tokenProvider.getUserIdFromToken(accessToken);

        User user = userService.findByUserId(userId);

        Map<String, Boolean> result = new HashMap<>();

        if(user.getAddress() == null) {
            result.put("certifiedAddress", false);
        } else {
            result.put("certifiedAddress", true);
        }

        PetCard petCard = petCardService.findByUserId(user);
        if(petCard == null) {
            result.put("certifiedPetCard", false);
        } else {
            result.put("certifiedPetCard", true);
        }

        return ApiResponse.success("data", result);
    }

    /**
     * 동물 등록 카드 인증
     */
    @PostMapping("/pet-card")
    public ApiResponse certifyPetCard(HttpServletRequest request,
                                      @RequestPart(value="pet_card_number") String petCardNumber,
                                      @RequestPart(value="pet_card_image") MultipartFile petCardImage) {

        String accessToken = HeaderUtil.getAccessToken(request);
        Long userId = tokenProvider.getUserIdFromToken(accessToken);

        petCardService.createPetCardByUserId(userId, petCardImage, petCardNumber);

        return ApiResponse.success("data", null);
    }

    /**
     * 동네 등록
     */
    @PostMapping("/address")
    public ApiResponse certifyAddress(HttpServletRequest request,
                                      @RequestBody SignUpDto.Address address) {
        String accessToken = HeaderUtil.getAccessToken(request);
        Long userId = tokenProvider.getUserIdFromToken(accessToken);

        User user = userService.findByUserId(userId);
        user.updateAddress(address.getAddressList());
        userService.save(user);

        return ApiResponse.created("data", null);
    }

}
