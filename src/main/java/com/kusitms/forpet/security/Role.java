package com.kusitms.forpet.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    FORPET_USER("ROLE_FORPET_USER");

    private String value;
}
