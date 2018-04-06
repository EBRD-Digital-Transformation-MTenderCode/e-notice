package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.procurement.notice.exception.EnumException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Stage {
    EI,
    FS,
    PS,
    PQ,
    PN,
    PIN,
    EV,
    CT;

    private static final Map<String, Stage> CONSTANTS = new HashMap<>();

    @JsonCreator
    public static Stage fromValue(final String value) {
        final Stage constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new EnumException(Stage.class.getName(), value, Arrays.toString(values()));
        }
        return constant;
    }
}
