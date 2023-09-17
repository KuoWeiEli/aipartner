package com.owl.aipartner.model.user;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserAuthority {
    ADMIN, NORMAL;

    @JsonCreator
    public UserAuthority fromString(String key) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(key))
                .findFirst()
                .orElse(null);
    }
}
