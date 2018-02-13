package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum InitiationType {
    TENDER("tender");

    private final String value;
    private final static Map<String, InitiationType> CONSTANTS = new HashMap<>();

    static {
        for (final InitiationType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    InitiationType(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static InitiationType fromValue(final String value) {
        final InitiationType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        }
        return constant;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }
}