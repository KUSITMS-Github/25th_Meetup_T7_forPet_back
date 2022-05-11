package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class QnaBoardRequestDto {

    public String title;
    public String content;
    private List<String> hashTag = new ArrayList<>();   //해쉬태그 정보
}

