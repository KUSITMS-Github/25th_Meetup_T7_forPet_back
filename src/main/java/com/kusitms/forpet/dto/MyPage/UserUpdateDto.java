package com.kusitms.forpet.dto.MyPage;

import com.kusitms.forpet.dto.SignUpDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    private String nickname;
    private SignUpDto.Address address;
}
