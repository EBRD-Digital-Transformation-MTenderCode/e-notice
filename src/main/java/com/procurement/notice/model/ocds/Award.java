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
        "title",
        "description",
        "status",
        "date",
        "value",
        "suppliers",
        "items",
        "contractPeriod",
        "documents",
        "amendments",
        "amendment",
        "relatedLots",
        "requirementResponses",
        "reviewProceedings",
        "statusDetails"
})
public class Award {
    @JsonProperty("title")
    @JsonPropertyDescription("Award title")
    private final String title;
    @JsonProperty("description")
    @JsonPropertyDescription("Award description")
    private final String description;
    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the award drawn from the [awardStatus codelist](http://standard" +
            ".open-contracting.org/latest/en/schema/codelists/#award-status)")
    private final Status status;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date of the contract award. This is usually the date on which a decision to award " +
            "was made.")
    private final LocalDateTime date;
    @JsonProperty("value")
    private final Value value;
    @JsonProperty("suppliers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The suppliers awarded this award. If different suppliers have been awarded different " +
            "items of values, these should be split into separate award blocks.")
    private final Set<OrganizationReference> suppliers;
    @JsonProperty("items")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods and services awarded in this award, broken into line items wherever possible." +
            " Items should not be duplicated, but the quantity specified instead.")
    private final Set<Item> items;
    @JsonProperty("contractPeriod")
    private final Period contractPeriod;
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the award, including any notices.")
    private final Set<Document> documents;
    @JsonProperty("amendments")
    @JsonPropertyDescription("An award amendment is a formal change to the details of the award, and generally " +
            "involves the publication of a new award notice/release. The rationale and a description of the changes " +
            "made " +
            "can be provided here.")
    private final List<Amendment> amendments;
    @JsonProperty("amendment")
    @JsonPropertyDescription("Amendment information")
    private final Amendment amendment;
    @JsonProperty("relatedLots")
    @JsonPropertyDescription("If this award relates to one or more specific lots, provide the identifier(s) of the " +
            "related lot(s) here.")
    private final List<String> relatedLots;
    @JsonProperty("requirementResponses")
    private final Set<RequirementResponse> requirementResponses;
    @JsonProperty("reviewProceedings")
    private final ReviewProceedings reviewProceedings;
    @JsonProperty("statusDetails")
    @JsonPropertyDescription("Additional details of an award status.")
    private final String statusDetails;
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier for this award. It must be unique and cannot change within the Open " +
            "Contracting Process it is part of (defined by a single ocid). See the [identifier guidance]" +
            "(http://standard" +
            ".open-contracting.org/latest/en/schema/identifiers/) for further details.")
    private String id;

    @JsonCreator
    public Award(@JsonProperty("id") final String id,
                 @JsonProperty("title") final String title,
                 @JsonProperty("description") final String description,
                 @JsonProperty("status") final Status status,
                 @JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime
                         date,
                 @JsonProperty("value") final Value value,
                 @JsonProperty("suppliers") final LinkedHashSet<OrganizationReference> suppliers,
                 @JsonProperty("items") final LinkedHashSet<Item> items,
                 @JsonProperty("contractPeriod") final Period contractPeriod,
                 @JsonProperty("documents") final LinkedHashSet<Document> documents,
                 @JsonProperty("amendments") final List<Amendment> amendments,
                 @JsonProperty("amendment") final Amendment amendment,
                 @JsonProperty("relatedLots") final List<String> relatedLots,
                 @JsonProperty("requirementResponses") final LinkedHashSet<RequirementResponse> requirementResponses,
                 @JsonProperty("reviewProceedings") final ReviewProceedings reviewProceedings,
                 @JsonProperty("statusDetails") final String statusDetails) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.date = date;
        this.value = value;
        this.suppliers = suppliers;
        this.items = items;
        this.contractPeriod = contractPeriod;
        this.documents = documents;
        this.amendments = amendments;
        this.amendment = amendment;
        this.relatedLots = relatedLots;
        this.requirementResponses = requirementResponses;
        this.reviewProceedings = reviewProceedings;
        this.statusDetails = statusDetails;
    }

    public enum Status {
        PENDING("pending"),
        ACTIVE("active"),
        CANCELLED("cancelled"),
        UNSUCCESSFUL("unsuccessful");

        private final static Map<String, Status> CONSTANTS = new HashMap<>();

        static {
            for (final Status c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        Status(final String value) {
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
