package com.kusitms.forpet.dto.Community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentParentResDto {
    private Long id;            //댓글 고유 id
    private String imageUrl;
    private String nickName;
    private String comment;
    private String createDate;
    private int likes;
    private List<CommentChildResDto> childResDto;
}
