package com.kusitms.forpet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    /**
     * 카테고리
     */
    MEETING("meeting"),
    SHARING("sharing"),
    BOASTING("boasting"),
    ALL("");

    private String value;
}
