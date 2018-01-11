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
        "awardID",
        "extendsContractID",
        "title",
        "description",
        "status",
        "period",
        "value",
        "items",
        "dateSigned",
        "documents",
        "implementation",
        "relatedProcesses",
        "milestones",
        "amendments",
        "amendment",
        "requirementResponses",
        "countryOfOrigin",
        "lotVariant",
        "valueBreakdown",
        "isFrameworkOrDynamic"
})
public class Contract {
    @JsonProperty("awardID")
    @JsonPropertyDescription("The award.id against which this contract is being issued.")
    private final String awardID;
    @JsonProperty("extendsContractID")
    @JsonPropertyDescription("If this contract extends or amends a previously issued contract, then the contract.id " +
            "value for the extended/amended contract can be provided here.")
    private final String extendsContractID;
    @JsonProperty("title")
    @JsonPropertyDescription("Contract title")
    private final String title;
    @JsonProperty("description")
    @JsonPropertyDescription("Contract description")
    private final String description;
    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the contract. Drawn from the [contractStatus codelist]" +
            "(http://standard.open-contracting.org/latest/en/schema/codelists/#contract-status)")
    private final Status status;
    @JsonProperty("period")
    private final Period period;
    @JsonProperty("value")
    private final Value value;
    @JsonProperty("items")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods, services, and any intangible outcomes in this contract. Note: If the items " +
            "are the same as the award do not repeat.")
    private final Set<Item> items;
    @JsonProperty("dateSigned")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date the contract was signed. In the case of multiple signatures, the date of the " +
            "last signature.")
    private final LocalDateTime dateSigned;
    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("All documents and attachments related to the contract, including any notices.")
    private final Set<Document> documents;
    @JsonProperty("implementation")
    @JsonPropertyDescription("Information during the performance / implementation stage of the contract.")
    private final Implementation implementation;
    @JsonProperty("relatedProcesses")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("If this process is followed by one or more contracting processes, represented under a " +
            "separate open contracting identifier (ocid) then details of the related process can be provided here. " +
            "This " +
            "is commonly used to point to subcontracts, or to renewal and replacement processes for this contract.")
    private final Set<RelatedProcess> relatedProcesses;
    @JsonProperty("milestones")
    @JsonPropertyDescription("A list of milestones associated with the finalization of this contract.")
    private final List<Milestone> milestones;
    @JsonProperty("amendments")
    @JsonPropertyDescription("A contract amendment is a formal change to, or extension of, a contract, and generally " +
            "involves the publication of a new contract notice/release, or some other documents detailing the change." +
            " The" +
            " rationale and a description of the changes made can be provided here.")
    private final List<Amendment> amendments;
    @JsonProperty("amendment")
    @JsonPropertyDescription("Amendment information")
    private final Amendment amendment;
    @JsonProperty("requirementResponses")
    private final Set<RequirementResponse> requirementResponses;
    @JsonProperty("countryOfOrigin")
    @JsonPropertyDescription("Country of origin of the product or service. Required by EU.")
    private final String countryOfOrigin;
    @JsonProperty("lotVariant")
    @JsonPropertyDescription("The contract was awarded to a tenderer who submitted a variant for the following lot(s)")
    private final Set<String> lotVariant;
    @JsonProperty("valueBreakdown")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The breakdown of known and/or estimated values in this contract.")
    private final Set<ValueBreakdown> valueBreakdown;
    @JsonProperty("isFrameworkOrDynamic")
    @JsonPropertyDescription("Contracts within a framework agreement or a dynamic purchasing system are being awarded" +
            ". Required by the EU")
    private final Boolean isFrameworkOrDynamic;
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier for this contract. It must be unique and cannot change within its Open " +
            "Contracting Process (defined by a single ocid). See the [identifier guidance](http://standard" +
            ".open-contracting.org/latest/en/schema/identifiers/) for further details.")
    private String id;

    @JsonCreator
    public Contract(@JsonProperty("id") final String id,
                    @JsonProperty("awardID") final String awardID,
                    @JsonProperty("extendsContractID") final String extendsContractID,
                    @JsonProperty("title") final String title,
                    @JsonProperty("description") final String description,
                    @JsonProperty("status") final Status status,
                    @JsonProperty("period") final Period period,
                    @JsonProperty("value") final Value value,
                    @JsonProperty("items") final LinkedHashSet<Item> items,
                    @JsonProperty("dateSigned") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                    LocalDateTime dateSigned,
                    @JsonProperty("documents") final LinkedHashSet<Document> documents,
                    @JsonProperty("implementation") final Implementation implementation,
                    @JsonProperty("relatedProcesses") final LinkedHashSet<RelatedProcess> relatedProcesses,
                    @JsonProperty("milestones") final List<Milestone> milestones,
                    @JsonProperty("amendments") final List<Amendment> amendments,
                    @JsonProperty("amendment") final Amendment amendment,
                    @JsonProperty("requirementResponses") final LinkedHashSet<RequirementResponse> requirementResponses,
                    @JsonProperty("countryOfOrigin") final String countryOfOrigin,
                    @JsonProperty("lotVariant") final LinkedHashSet<String> lotVariant,
                    @JsonProperty("valueBreakdown") final LinkedHashSet<ValueBreakdown> valueBreakdown,
                    @JsonProperty("isFrameworkOrDynamic") final Boolean isFrameworkOrDynamic) {
        this.id = id;
        this.awardID = awardID;
        this.extendsContractID = extendsContractID;
        this.title = title;
        this.description = description;
        this.status = status;
        this.period = period;
        this.value = value;
        this.items = items;
        this.dateSigned = dateSigned;
        this.documents = documents;
        this.implementation = implementation;
        this.relatedProcesses = relatedProcesses;
        this.milestones = milestones;
        this.amendments = amendments;
        this.amendment = amendment;
        this.requirementResponses = requirementResponses;
        this.countryOfOrigin = countryOfOrigin;
        this.lotVariant = lotVariant;
        this.valueBreakdown = valueBreakdown;
        this.isFrameworkOrDynamic = isFrameworkOrDynamic;
    }

    public enum Status {
        PENDING("pending"),
        ACTIVE("active"),
        CANCELLED("cancelled"),
        TERMINATED("terminated");

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
