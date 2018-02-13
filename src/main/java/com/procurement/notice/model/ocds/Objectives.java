
package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "types",
    "additionalInformation"
})
public class Objectives {
    @JsonProperty("types")
    @JsonPropertyDescription("Contract performance conditions with environmental, social and innovative objectives. " +
        "Required by the EU.")
    @Valid
    private final List<ObjectivesType> types;

    @JsonProperty("additionalInformation")
    @JsonPropertyDescription("Further details about the objectives of the procurement. Required by the EU")
    private final String additionalInformation;

    @JsonCreator
    public Objectives(@JsonProperty("types") final List<ObjectivesType> types,
                      @JsonProperty("additionalInformation") final String additionalInformation) {
        this.types = types;
        this.additionalInformation = additionalInformation;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(types)
                                    .append(additionalInformation)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Objectives)) {
            return false;
        }
        final Objectives rhs = (Objectives) other;
        return new EqualsBuilder().append(types, rhs.types)
                                  .append(additionalInformation, rhs.additionalInformation)
                                  .isEquals();
    }

    public enum ObjectivesType {
        ENVIRONMENTAL("environmental"),
        SOCIAL("social"),
        INNOVATIVE("innovative");
        private final String value;
        private final static Map<String, ObjectivesType> CONSTANTS = new HashMap<>();

        static {
            for (final ObjectivesType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private ObjectivesType(final String value) {
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
        public static ObjectivesType fromValue(final String value) {
            final ObjectivesType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            }
            return constant;
        }

    }
}
