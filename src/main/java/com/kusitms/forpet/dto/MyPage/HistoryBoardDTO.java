package com.kusitms.forpet.dto.MyPage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryBoardDTO {
    private Long id;
    private String category;
    private String title;
    private int likes;
    private int comments;
}
