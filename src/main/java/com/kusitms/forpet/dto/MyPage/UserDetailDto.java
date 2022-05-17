package com.kusitms.forpet.dto.MyPage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kusitms.forpet.dto.SignUpDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailDto {
    private Long id;
    @JsonProperty(value = "is_certified_address")
    private boolean isCertifiedAddress;
    @JsonProperty(value = "is_certified_pet_card")
    private boolean isCertifiedPetCard;
    private String[] addressList;
    private String nickname;
    private String name;
    @JsonProperty(value = "profile_image_url")
    private String profileImageUrl;
}
