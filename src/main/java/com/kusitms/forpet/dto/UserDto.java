package com.kusitms.forpet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private String imageUrl;

    private String nickname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("pet_card_number")
    private String petCardNumber;
    private SignUpDto.Address address;

    private String petCardImageUrl;
    private String customImageUrl;

    @JsonProperty("access_token")
    private String accessToken;

}
