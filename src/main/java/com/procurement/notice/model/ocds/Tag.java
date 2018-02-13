
package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum Tag {
    PLANNING("planning"),
    PLANNING_UPDATE("planningUpdate"),
    TENDER("tender"),
    TENDER_AMENDMENT("tenderAmendment"),
    TENDER_UPDATE("tenderUpdate"),
    TENDER_CANCELLATION("tenderCancellation"),
    AWARD("award"),
    AWARD_UPDATE("awardUpdate"),
    AWARD_CANCELLATION("awardCancellation"),
    CONTRACT("contract"),
    CONTRACT_UPDATE("contractUpdate"),
    CONTRACT_AMENDMENT("contractAmendment"),
    IMPLEMENTATION("implementation"),
    IMPLEMENTATION_UPDATE("implementationUpdate"),
    CONTRACT_TERMINATION("contractTermination"),
    COMPILED("compiled");

    private final String value;
    private final static Map<String, Tag> CONSTANTS = new HashMap<>();

    static {
        for (final Tag c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Tag(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static Tag fromValue(final String value) {
        final Tag constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        }
        return constant;
    }
}
