package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "date",
        "status",
        "tenderers",
        "value",
        "documents",
        "relatedLots",
        "requirementResponses"
})
public class Bid {
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date when this bid was received.")
    private final LocalDateTime date;
    @JsonProperty("status")
    @JsonPropertyDescription("The status of the bid, drawn from the bidStatus codelist")
    private final Status status;
    @JsonProperty("tenderers")
    @JsonPropertyDescription("The party, or parties, responsible for this bid. This should provide a name and " +
            "identifier, cross-referenced to an entry in the parties array at the top level of the release.")
    private final List<OrganizationReference> tenderers;
    @JsonProperty("value")
    private final Value value;
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the bid and its evaluation.")
    private final Set<Document> documents;
    @JsonProperty("relatedLots")
    @JsonPropertyDescription("If this bid relates to one or more specific lots, provide the identifier(s) of the " +
            "related lot(s) here.")
    private final List<String> relatedLots;
    @JsonProperty("requirementResponses")
    private final Set<RequirementResponse> requirementResponses;
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this bid")
    private String id;

    @JsonCreator
    public Bid(@JsonProperty("id") final String id,
               @JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime date,
               @JsonProperty("status") final Status status,
               @JsonProperty("tenderers") final List<OrganizationReference> tenderers,
               @JsonProperty("value") final Value value,
               @JsonProperty("documents") final LinkedHashSet<Document> documents,
               @JsonProperty("relatedLots") final List<String> relatedLots,
               @JsonProperty("requirementResponses") final LinkedHashSet<RequirementResponse> requirementResponses) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.tenderers = tenderers;
        this.value = value;
        this.documents = documents;
        this.relatedLots = relatedLots;
        this.requirementResponses = requirementResponses;
    }

    public enum Status {
        INVITED("invited"),
        PENDING("pending"),
        VALID("valid"),
        DISQUALIFIED("disqualified"),
        WITHDRAWN("withdrawn");

        private final static Map<String, Status> CONSTANTS = new HashMap<>();

        static {
            for (final Status c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static Status fromValue(final String value) {
            final Status constant = CONSTANTS.get(value);
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
