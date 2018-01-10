package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "value",
        "description",
        "methodOfPayment"
})
public class ParticipationFee {
    @JsonProperty("type")
    @JsonPropertyDescription("A fees applicable to bidders wishing to participate in the tender process. Fees may " +
            "apply for access to bidding documents, for the submission of bids or there may be a win fee payable by " +
            "the " +
            "successful bidder.")
    private final List<ParticipationFeeType> type;

    @JsonProperty("value")
    private final Value value;

    @JsonProperty("description")
    @JsonPropertyDescription("Optional information about the way in which fees are levied, or the exact nature of the" +
            " fees.")
    private final String description;

    @JsonProperty("methodOfPayment")
    @JsonPropertyDescription("Optional information about the way in which fees can be paid.")
    private final List<String> methodOfPayment;

    public ParticipationFee(@JsonProperty("type") final List<ParticipationFeeType> type,
                            @JsonProperty("value") final Value value,
                            @JsonProperty("description") final String description,
                            @JsonProperty("methodOfPayment") final List<String> methodOfPayment) {
        this.type = type;
        this.value = value;
        this.description = description;
        this.methodOfPayment = methodOfPayment;
    }

    public enum ParticipationFeeType {
        DOCUMENT("document"),
        DEPOSIT("deposit"),
        SUBMISSION("submission"),
        WIN("win");
        private final static Map<String, ParticipationFeeType> CONSTANTS = new HashMap<>();

        static {
            for (final ParticipationFeeType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private ParticipationFeeType(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static ParticipationFeeType fromValue(final String value) {
            final ParticipationFeeType constant = CONSTANTS.get(value);
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
