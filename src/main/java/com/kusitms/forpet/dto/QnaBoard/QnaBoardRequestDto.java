package com.kusitms.forpet.dto.QnaBoard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class QnaBoardRequestDto {

    public String title;
    public String content;
}

