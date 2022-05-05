package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private int star;
    private String content;
    private String writer;
    private LocalDateTime createDate;

}
