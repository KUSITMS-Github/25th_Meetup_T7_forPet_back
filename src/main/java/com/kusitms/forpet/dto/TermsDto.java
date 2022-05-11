package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TermsDto {
    private Long termsId;
    private String name;
    private String content;
    private String is_required;
}
