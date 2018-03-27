package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum RelatedProcessType {

    FRAMEWORK("framework"),
    PLANNING("planning"),
    PARENT("parent"),
    PRIOR("prior"),
    UNSUCCESSFUL_PROCESS("unsuccessfulProcess"),
    REPLACEMENT_PROCESS("replacementProcess"),
    RENEWAL_PROCESS("renewalProcess"),
    SUB_CONTRACT("subContract"),
    X_EXPENDITURE_ITEM("x_expenditureItem"),
    X_FINANCE_SOURCE("x_financeSource"),
    X_PRESELECTION("x_preselection"),
    X_PREQUALIFICATION("x_prequalification"),
    X_EVALUATION("x_evaluation"),
    X_EXECUTION("x_execution"),
    X_PLANNED("x_planned"),
    X_BUDGET("x_budget");

    private final String value;
    private final static Map<String, RelatedProcessType> CONSTANTS = new HashMap<>();

    static {
        for (final RelatedProcessType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    RelatedProcessType(final String value) {
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
    public static RelatedProcessType fromValue(final String value) {
        final RelatedProcessType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        }
        return constant;
    }
}

