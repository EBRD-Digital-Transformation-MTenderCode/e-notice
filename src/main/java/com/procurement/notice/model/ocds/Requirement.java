package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "dataType",
        "pattern",
        "expectedValue",
        "minValue",
        "maxValue",
        "period"
})
public class Requirement {
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier for this requirement. It must be unique and cannot change within the " +
            "Open Contracting Process it is part of (defined by a single ocid). See the [identifier guidance]" +
            "(http://standard.open-contracting.org/latest/en/schema/identifiers/) for further details.")
    private final String id;

    @JsonProperty("title")
    @JsonPropertyDescription("Requirement title")
    private final String title;

    @JsonProperty("description")
    @JsonPropertyDescription("Requirement description")
    private final String description;

    @JsonProperty("dataType")
    @JsonPropertyDescription("Requirement description")
    private final DataType dataType;

    @JsonProperty("pattern")
    @JsonPropertyDescription("A regular expression against which validate the requirement response")
    private final String pattern;

    @JsonProperty("expectedValue")
    @JsonPropertyDescription("Used to state the requirement when the response must be particular value")
    private final String expectedValue;

    @JsonProperty("minValue")
    @JsonPropertyDescription("Used to state the lower bound of the requirement when the response must be within a " +
            "certain range")
    private final Double minValue;

    @JsonProperty("maxValue")
    @JsonPropertyDescription("Used to state the upper bound of the requirement when the response must be within a " +
            "certain range")
    private final Double maxValue;

    @JsonProperty("period")
    private final Period period;

    @JsonCreator
    public Requirement(@JsonProperty("id") final String id,
                       @JsonProperty("title") final String title,
                       @JsonProperty("description") final String description,
                       @JsonProperty("dataType") final DataType dataType,
                       @JsonProperty("pattern") final String pattern,
                       @JsonProperty("expectedValue") final String expectedValue,
                       @JsonProperty("minValue") final Double minValue,
                       @JsonProperty("maxValue") final Double maxValue,
                       @JsonProperty("period") final Period period) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dataType = dataType;
        this.pattern = pattern;
        this.expectedValue = expectedValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.period = period;
    }

    public enum DataType {
        STRING("string"),
        DATE_TIME("date-time"),
        NUMBER("number"),
        INTEGER("integer");
        private final static Map<String, DataType> CONSTANTS = new HashMap<>();

        static {
            for (final Requirement.DataType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private DataType(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static Requirement.DataType fromValue(final String value) {
            final Requirement.DataType constant = CONSTANTS.get(value);
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
