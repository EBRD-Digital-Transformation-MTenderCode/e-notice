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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "statusDetails",
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
        "reviewProceedings"
})
public class Award {
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier for this award. It must be unique and cannot change within the Open " +
            "Contracting Process it is part of (defined by a single ocid). See the [identifier guidance]" +
            "(http://standard" +
            ".open-contracting.org/latest/en/schema/identifiers/) for further details.")
    @Size(min = 1)
    @NotNull
    private final String id;

    @JsonProperty("title")
    @JsonPropertyDescription("Award title")
    private final String title;

    @JsonProperty("description")
    @JsonPropertyDescription("Award description")
    private String description;

    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the award drawn from the [awardStatus codelist](http://standard" +
            ".open-contracting.org/latest/en/schema/codelists/#award-status)")
    private Status status;


    @JsonProperty("statusDetails")
    @JsonPropertyDescription("Additional details of an award status.")
    private Status statusDetails;

    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date of the contract award. This is usually the date on which a decision to award " +
            "was made.")
    private final LocalDateTime date;

    @JsonProperty("value")
    @Valid
    private final Value value;

    @JsonProperty("suppliers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The suppliers awarded this award. If different suppliers have been awarded different " +
            "items of values, these should be split into separate award blocks.")
    @Valid
    private final Set<OrganizationReference> suppliers;

    @JsonProperty("items")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods and services awarded in this award, broken into line items wherever possible." +
            " Items should not be duplicated, but the quantity specified instead.")
    @Size(min = 1)
    @Valid
    private final Set<Item> items;

    @JsonProperty("contractPeriod")
    @Valid
    private final Period contractPeriod;

    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the award, including any notices.")
    @Valid
    private Set<Document> documents;

    @JsonProperty("amendments")
    @JsonPropertyDescription("An award amendment is a formal change to the details of the award, and generally " +
            "involves the publication of a new award notice/release. The rationale and a description of the changes " +
            "made " +
            "can be provided here.")
    @Valid
    private final List<Amendment> amendments;

    @JsonProperty("amendment")
    @JsonPropertyDescription("Amendment information")
    @Valid
    private final Amendment amendment;

    @JsonProperty("relatedLots")
    @JsonPropertyDescription("If this award relates to one or more specific lots, provide the identifier(s) of the " +
            "related lot(s) here.")
    private final List<String> relatedLots;

    @JsonProperty("requirementResponses")
    @Valid
    private final Set<RequirementResponse> requirementResponses;

    @JsonProperty("reviewProceedings")
    @Valid
    private final ReviewProceedings reviewProceedings;

    @JsonProperty("relatedBid")
    @JsonPropertyDescription("Where bid details are used, a cross reference to the entry in the bids array to which " +
            "this award relates. Provide the bid identifier here.")
    private final String relatedBid;

    @JsonCreator
    public Award(@JsonProperty("id") final String id,
                 @JsonProperty("title") final String title,
                 @JsonProperty("description") final String description,
                 @JsonProperty("status") final Status status,
                 @JsonProperty("statusDetails") final Status statusDetails,
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
                 @JsonProperty("relatedBid") final String relatedBid) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.statusDetails = statusDetails;
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
        this.relatedBid = relatedBid;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(title)
                .append(description)
                .append(status)
                .append(statusDetails)
                .append(date)
                .append(value)
                .append(suppliers)
                .append(items)
                .append(contractPeriod)
                .append(documents)
                .append(amendments)
                .append(amendment)
                .append(relatedLots)
                .append(requirementResponses)
                .append(reviewProceedings)
                 .append(relatedBid)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Award)) {
            return false;
        }
        final Award rhs = (Award) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(title, rhs.title)
                .append(description, rhs.description)
                .append(status, rhs.status)
                .append(statusDetails, rhs.statusDetails)
                .append(date, rhs.date)
                .append(value, rhs.value)
                .append(suppliers, rhs.suppliers)
                .append(items, rhs.items)
                .append(contractPeriod, rhs.contractPeriod)
                .append(documents, rhs.documents)
                .append(amendments, rhs.amendments)
                .append(amendment, rhs.amendment)
                .append(relatedLots, rhs.relatedLots)
                .append(requirementResponses, rhs.requirementResponses)
                .append(reviewProceedings, rhs.reviewProceedings)
                .append(relatedBid, rhs.relatedBid)
                .isEquals();
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
