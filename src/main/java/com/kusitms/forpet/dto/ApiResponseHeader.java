package com.kusitms.forpet.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class ApiResponseHeader {
    private int code;
    private String message;
}
