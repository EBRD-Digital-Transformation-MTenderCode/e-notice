package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "type",
        "description",
        "amount",
        "estimationMethod"
})
public class ValueBreakdown {
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier of this value breakdown. Unique within this array.")
    private final String id;

    @JsonProperty("type")
    @JsonPropertyDescription("A value from the [valueType codelist] (http://standard.open-contracting" +
            ".org/1.1-dev/en/schema/codelists/#value-type) that identifies the nature of the value in the breakdown. " +
            "Negative values indicate a revenue to the supplier.")
    private final List<ValueBreakdownType> type;

    @JsonProperty("description")
    @JsonPropertyDescription("The description of this value breakdown.")
    private final String description;

    @JsonProperty("amount")
    private final Value amount;

    @JsonProperty("estimationMethod")
    private final Value estimationMethod;

    @JsonCreator
    public ValueBreakdown(@JsonProperty("id") final String id,
                          @JsonProperty("type") final List<ValueBreakdownType> type,
                          @JsonProperty("description") final String description,
                          @JsonProperty("amount") final Value amount,
                          @JsonProperty("estimationMethod") final Value estimationMethod) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.estimationMethod = estimationMethod;
    }

    public enum ValueBreakdownType {
        USER_FEES("userFees"),
        PUBLIC_AGENCY_FEES("publicAgencyFees");
        private final static Map<String, ValueBreakdownType> CONSTANTS = new HashMap<>();

        static {
            for (final ValueBreakdownType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private ValueBreakdownType(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static ValueBreakdownType fromValue(final String value) {
            final ValueBreakdownType constant = CONSTANTS.get(value);
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
}
