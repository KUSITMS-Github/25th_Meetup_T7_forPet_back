package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserDto {
    private Long userId;
    private String name;
    private String email;
    private String imageUrl;
}
