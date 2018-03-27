package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum RelatedProcessScheme {

    OCID("ocid");

    private final String value;
    private final static Map<String, RelatedProcessScheme> CONSTANTS = new HashMap<>();

    static {
        for (final RelatedProcessScheme c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    RelatedProcessScheme(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static RelatedProcessScheme fromValue(final String value) {
        final RelatedProcessScheme constant = CONSTANTS.get(value);
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