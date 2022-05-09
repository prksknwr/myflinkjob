package com.myflinkjob.commons.models.models;

import java.util.Arrays;

public enum ContactBy {
    NONE ("none"),
    ALL ("all"),
    EMAIL("email"),
    PHONE ("phone");

    private String value;
    ContactBy(String value) {
        this.value = value;
    }

    public static ContactBy fromString(String name) {
        return Arrays.stream(values())
                .filter(contactBy -> contactBy.value.equals(name))
                .findFirst()
                .orElse(ContactBy.NONE);
    }
}
