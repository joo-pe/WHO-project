package com.who.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER"),
    GUEST("ROLE_GUEST");

    private String value;
}