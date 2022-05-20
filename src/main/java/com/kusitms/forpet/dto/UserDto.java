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
    @JsonProperty("image_url")
    private String imageUrl;

    private String nickname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("pet_card_number")
    private String[] address;

    @JsonProperty("pet_card_image_url")
    private String petCardImageUrl;
    @JsonProperty("custom_image_url")
    private String customImageUrl;

    private String token;

}
