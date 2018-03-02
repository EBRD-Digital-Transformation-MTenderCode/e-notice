package com.procurement.notice.model.tender.pspq;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.procurement.notice.model.ocds.*;
import com.procurement.notice.model.tender.enquiry.PsPqEnquiry;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
        "enquiryPeriod",
        "hasEnquiries",
        "enquiries",
        "amendments",
        "value",
        "lotGroups",
        "lots",
        "items",
        "awardCriteria",
        "requiresElectronicCatalogue",
        "submissionMethod",
        "submissionMethodDetails",
        "submissionMethodRationale",
        "tenderPeriod",
        "contractPeriod",
        "standstillPeriod",
        "documents"
})
public class PsPqTender {
    @JsonProperty("id")
    @JsonPropertyDescription("An identifier for this tender process. This may be the same as the ocid, or may be " +
            "drawn from an internally held identifier for this tender.")
    @Size(min = 1)
    @NotNull
    private final String id;

    @JsonProperty("title")
    @JsonPropertyDescription("A title for this tender. This will often be used by applications as a headline to " +
            "attract interest, and to help analysts understand the nature of this procurement.")
    private final String title;

    @JsonProperty("description")
    @JsonPropertyDescription("A summary description of the tender. This should complement structured information " +
            "provided using the items array. Descriptions should be short and easy to read. Avoid using ALL CAPS. ")
    private final String description;

    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the tender based on the [tenderStatus codelist](http://standard" +
            ".open-contracting.org/latest/en/schema/codelists/#tender-status)")
    private TenderStatus status;

    @JsonProperty("statusDetails")
    @JsonPropertyDescription("Additional details of status.)")
    private TenderStatusDetails statusDetails;

    @JsonProperty("enquiryPeriod")
    @Valid
    private Period enquiryPeriod;

    @JsonProperty("hasEnquiries")
    @JsonPropertyDescription("A true/false field to indicate whether any enquiries were received during the tender " +
            "process. Structured information on enquiries that were received, and responses to them, can be provided " +
            "using the enquiries extension.")
    private final Boolean hasEnquiries;

    @JsonProperty("enquiries")
    @JsonPropertyDescription("Enquiries array to tender, consisting of one or more enquiry objects, each with fields " +
            "for a question, and an answer.")
    private List<PsPqEnquiry> enquiries;

    @JsonProperty("amendments")
    @JsonPropertyDescription("A tender amendment is a formal change to the tender, and generally involves the " +
            "publication of a new tender notice/release. The rationale and a description of the changes made can be " +
            "provided here.")
    @Valid
    private final List<Amendment> amendments;

    @JsonProperty("value")
    @Valid
    private final Value value;

    @JsonProperty("lots")
    @JsonPropertyDescription("A tender process may be divided into lots, where bidders can bid on one or more lots. " +
            "Details of each lot can be provided here. Items, documents and other features can then reference the lot" +
            " " +
            "they are related to using relatedLot. Where no relatedLot identifier is given, the values should be " +
            "interpreted as applicable to the whole tender. Properties of tender can be overridden for a given Lot " +
            "through their inclusion in the Lot object.")
    @Valid
    private List<Lot> lots;

    @JsonProperty("lotGroups")
    @JsonPropertyDescription("ere the buyer reserves the right to combine lots, or wishes to specify the total value " +
            "for a group of lots, a lot group is used to capture this information.")
    @Valid
    private final List<LotGroup> lotGroups;

    @JsonProperty("items")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods and services to be purchased, broken into line items wherever possible. Items" +
            " should not be duplicated, but a quantity of 2 specified instead.")
    @Valid
    private final Set<Item> items;

    @JsonProperty("awardCriteria")
    @JsonPropertyDescription("Specify the award criteria for the procurement, using the [award criteria codelist]" +
            "(http://standard.open-contracting.org/latest/en/schema/codelists/#award-criteria)")
    private final AwardCriteria awardCriteria;

    @JsonProperty("requiresElectronicCatalogue")
    @JsonPropertyDescription("Tenders must include an electronic catalogue. Required by the EU")
    private final Boolean requiresElectronicCatalogue;

    @JsonProperty("submissionMethod")
    @JsonPropertyDescription("Specify the method by which bids must be submitted, in person, written, or electronic " +
            "auction. Using the [submission method codelist](http://standard.open-contracting" +
            ".org/latest/en/schema/codelists/#submission-method)")
    private final List<SubmissionMethod> submissionMethod;

    @JsonProperty("submissionMethodDetails")
    @JsonPropertyDescription("Any detailed or further information on the submission method. This may include the " +
            "address, e-mail address or online service to which bids should be submitted, and any special " +
            "requirements to" +
            " be followed for submissions.")
    private final String submissionMethodDetails;

    @JsonProperty("submissionMethodRationale")
    @JsonPropertyDescription("A value from the [submissionValueRationale codelist](http://standard.open-contracting" +
            ".org/1.1-dev/en/schema/codelists/submission-method-rationale) that identifies the rationale where " +
            "electronic" +
            " submission method is not to be allowed. Required by EU.")
    @Valid
    private final List<SubmissionMethodRationale> submissionMethodRationale;

    @JsonProperty("tenderPeriod")
    @Valid
    private Period tenderPeriod;

    @JsonProperty("contractPeriod")
    @Valid
    private final Period contractPeriod;

    @JsonProperty("standstillPeriod")
    @Valid
    private Period standstillPeriod;

    @JsonProperty("documents")
    @JsonPropertyDescription("All documents and attachments related to the tender, including any notices. See the " +
            "[documentType codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#document-type) " +
            "for " +
            "details of potential documents to include. Common documents include official legal notices of tender, " +
            "technical specifications, evaluation criteria, and, as a tender process progresses, clarifications and " +
            "replies to queries.")
    @Valid
    private final List<Document> documents;


    @JsonCreator
    public PsPqTender(@JsonProperty("id") final String id,
                      @JsonProperty("title") final String title,
                      @JsonProperty("description") final String description,
                      @JsonProperty("status") final TenderStatus status,
                      @JsonProperty("statusDetails") final TenderStatusDetails statusDetails,
                      @JsonProperty("items") final LinkedHashSet<Item> items,
                      @JsonProperty("value") final Value value,
                      @JsonProperty("awardCriteria") final AwardCriteria awardCriteria,
                      @JsonProperty("submissionMethod") final List<SubmissionMethod> submissionMethod,
                      @JsonProperty("submissionMethodDetails") final String submissionMethodDetails,
                      @JsonProperty("tenderPeriod") final Period tenderPeriod,
                      @JsonProperty("enquiryPeriod") final Period enquiryPeriod,
                      @JsonProperty("hasEnquiries") final Boolean hasEnquiries,
                      @JsonProperty("enquiries") final List<PsPqEnquiry> enquiries,
                      @JsonProperty("contractPeriod") final Period contractPeriod,
                      @JsonProperty("documents") final List<Document> documents,
                      @JsonProperty("amendments") final List<Amendment> amendments,
                      @JsonProperty("lots") final List<Lot> lots,
                      @JsonProperty("lotGroups") final List<LotGroup> lotGroups,
                      @JsonProperty("standstillPeriod") final Period standstillPeriod,
                      @JsonProperty("submissionMethodRationale") final List<SubmissionMethodRationale>
                          submissionMethodRationale,
                      @JsonProperty("requiresElectronicCatalogue") final Boolean requiresElectronicCatalogue) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.statusDetails = statusDetails;
        this.items = items;
        this.value = value;
        this.awardCriteria = awardCriteria;
        this.submissionMethod = submissionMethod;
        this.submissionMethodDetails = submissionMethodDetails;
        this.tenderPeriod = tenderPeriod;
        this.enquiryPeriod = enquiryPeriod;
        this.hasEnquiries = hasEnquiries;
        this.enquiries = enquiries;
        this.contractPeriod = contractPeriod;
        this.documents = documents;
        this.amendments = amendments;
        this.lots = lots;
        this.lotGroups = lotGroups;
        this.standstillPeriod = standstillPeriod;
        this.submissionMethodRationale = submissionMethodRationale;
        this.requiresElectronicCatalogue = requiresElectronicCatalogue;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(title)
                .append(description)
                .append(status)
                .append(statusDetails)
                .append(items)
                .append(value)
                .append(awardCriteria)
                .append(submissionMethod)
                .append(submissionMethodDetails)
                .append(tenderPeriod)
                .append(enquiryPeriod)
                .append(hasEnquiries)
                .append(enquiries)
                .append(contractPeriod)
                .append(documents)
                .append(amendments)
                .append(lots)
                .append(lotGroups)
                .append(standstillPeriod)
                .append(submissionMethodRationale)
                .append(requiresElectronicCatalogue)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PsPqTender)) {
            return false;
        }
        final PsPqTender rhs = (PsPqTender) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(title, rhs.title)
                .append(description, rhs.description)
                .append(status, rhs.status)
                .append(statusDetails, rhs.statusDetails)
                .append(items, rhs.items)
                .append(value, rhs.value)
                .append(awardCriteria, rhs.awardCriteria)
                .append(submissionMethod, rhs.submissionMethod)
                .append(submissionMethodDetails, rhs.submissionMethodDetails)
                .append(tenderPeriod, rhs.tenderPeriod)
                .append(enquiryPeriod, rhs.enquiryPeriod)
                .append(hasEnquiries, rhs.hasEnquiries)
                .append(enquiries, rhs.enquiries)
                .append(contractPeriod, rhs.contractPeriod)
                .append(documents, rhs.documents)
                .append(amendments, rhs.amendments)
                .append(lots, rhs.lots)
                .append(lotGroups, rhs.lotGroups)
                .append(standstillPeriod, rhs.standstillPeriod)
                .append(submissionMethodRationale, rhs.submissionMethodRationale)
                .append(requiresElectronicCatalogue, rhs.requiresElectronicCatalogue)
                .isEquals();
    }

    public enum AwardCriteria {
        PRICE_ONLY("priceOnly"),
        COST_ONLY("costOnly"),
        QUALITY_ONLY("qualityOnly"),
        RATED_CRITERIA("ratedCriteria"),
        LOWEST_COST("lowestCost"),
        BEST_PROPOSAL("bestProposal"),
        BEST_VALUE_TO_GOVERNMENT("bestValueToGovernment"),
        SINGLE_BID_ONLY("singleBidOnly");

        private final static Map<String, AwardCriteria> CONSTANTS = new HashMap<>();

        static {
            for (final AwardCriteria c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        AwardCriteria(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static AwardCriteria fromValue(final String value) {
            final AwardCriteria constant = CONSTANTS.get(value);
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

    public enum SubmissionMethod {
        ELECTRONIC_SUBMISSION("electronicSubmission"),
        ELECTRONIC_AUCTION("electronicAuction"),
        WRITTEN("written"),
        IN_PERSON("inPerson");

        private final static Map<String, SubmissionMethod> CONSTANTS = new HashMap<>();

        static {
            for (final SubmissionMethod c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        SubmissionMethod(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static SubmissionMethod fromValue(final String value) {
            final SubmissionMethod constant = CONSTANTS.get(value);
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

    public enum SubmissionMethodRationale {
        TOOLS_DEVICES_FILE_FORMATS_UNAVAILABLE("TOOLS_DEVICES_FILE_FORMATS_UNAVAILABLE"),
        IPR_ISSUES("IPR_ISSUES"),
        REQUIRES_SPECIALISED_EQUIPMENT("REQUIRES_SPECIALISED_EQUIPMENT"),
        PHYSICAL_MODEL("PHYSICAL_MODEL"),
        SENSITIVE_INFORMATION("SENSITIVE_INFORMATION");

        private final static Map<String, SubmissionMethodRationale> CONSTANTS = new HashMap<>();

        static {
            for (final SubmissionMethodRationale c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        SubmissionMethodRationale(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static SubmissionMethodRationale fromValue(final String value) {
            final SubmissionMethodRationale constant = CONSTANTS.get(value);
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
