package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private String imageUrl;
    private String nickname;
    private String phone;
    private String address1; // 동네 3개
    private String address2;
    private String address3;
}
