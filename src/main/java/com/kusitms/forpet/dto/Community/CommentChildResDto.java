package com.kusitms.forpet.dto.Community;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentChildResDto {
    private Long id;
    private Long parentId;
    private String content;
}
