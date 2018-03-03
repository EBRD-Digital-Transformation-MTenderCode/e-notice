
package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "date",
        "status",
        "statusDetails",
        "tenderers",
        "value",
        "documents",
        "relatedLots",
        "requirementResponses"
})
public class Bid {
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this bid")
    @NotNull
    private final String id;

    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date when this bid was received.")
    private LocalDateTime date;

    @JsonProperty("status")
    @JsonPropertyDescription("The status of the bid, drawn from the bidStatus codelist")
    private Status status;

    @JsonProperty("statusDetails")
    private StatusDetails statusDetails;

    @JsonProperty("tenderers")
    @JsonPropertyDescription("The party, or parties, responsible for this bid. This should provide a name and " +
            "identifier, cross-referenced to an entry in the parties array at the top level of the release.")
    private final List<OrganizationReference> tenderers;

    @JsonProperty("value")
    @Valid
    private final Value value;

    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the bid and its evaluation.")
    @Valid
    private final Set<Document> documents;

    @JsonProperty("relatedLots")
    @JsonPropertyDescription("If this bid relates to one or more specific lots, provide the identifier(s) of the " +
            "related lot(s) here.")
    private final List<String> relatedLots;

    @JsonProperty("requirementResponses")
    @Valid
    private final Set<RequirementResponse> requirementResponses;

    @JsonCreator
    public Bid(@JsonProperty("id") final String id,
               @JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime date,
               @JsonProperty("status") final Status status,
               @JsonProperty("statusDetails") final StatusDetails statusDetails,
               @JsonProperty("tenderers") final List<OrganizationReference> tenderers,
               @JsonProperty("value") final Value value,
               @JsonProperty("documents") final LinkedHashSet<Document> documents,
               @JsonProperty("relatedLots") final List<String> relatedLots,
               @JsonProperty("requirementResponses") final LinkedHashSet<RequirementResponse> requirementResponses) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.statusDetails = statusDetails;
        this.tenderers = tenderers;
        this.value = value;
        this.documents = documents;
        this.relatedLots = relatedLots;
        this.requirementResponses = requirementResponses;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(date)
                .append(status)
                .append(statusDetails)
                .append(tenderers)
                .append(value)
                .append(documents)
                .append(relatedLots)
                .append(requirementResponses)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Bid)) {
            return false;
        }
        final Bid rhs = (Bid) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(date, rhs.date)
                .append(status, rhs.status)
                .append(statusDetails, rhs.statusDetails)
                .append(tenderers, rhs.tenderers)
                .append(value, rhs.value)
                .append(documents, rhs.documents)
                .append(relatedLots, rhs.relatedLots)
                .append(requirementResponses, rhs.requirementResponses)
                .isEquals();
    }

    public enum Status {
        INVITED("invited"),
        PENDING("pending"),
        VALID("valid"),
        DISQUALIFIED("disqualified"),
        WITHDRAWN("withdrawn");

        private static final Map<String, Status> CONSTANTS = new HashMap<>();
        private final String value;

        static {
            for (final Status c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Status(final String value) {
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
        public static Status fromValue(final String value) {
            final Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(
                        "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
            }
            return constant;
        }
    }

    public enum StatusDetails {
        VALID("valid"),
        DISQUALIFIED("disqualified");

        private static final Map<String, StatusDetails> CONSTANTS = new HashMap<String, StatusDetails>();
        private final String value;

        static {
            for (final StatusDetails c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        StatusDetails(final String value) {
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
        public static StatusDetails fromValue(final String value) {
            final StatusDetails constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(
                        "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
            }
            return constant;
        }
    }
}
