package com.kusitms.forpet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TermsDto {
    private Long termsId;
    private String name;
    private String content;
    private String is_required;
}
