package com.kusitms.forpet.exception;

import com.kusitms.forpet.dto.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    // 전역으로 사용할 exception
    private final ErrorCode errorCode;
}
