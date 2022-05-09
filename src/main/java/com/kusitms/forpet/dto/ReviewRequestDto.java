package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRequestDto {
    private int star;
    private String content;
}
