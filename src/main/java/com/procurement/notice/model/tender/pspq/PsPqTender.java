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
    @Size(min = 1)
    @NotNull
    private final String id;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("status")
    private TenderStatus status;
    @JsonProperty("statusDetails")
    private TenderStatusDetails statusDetails;
    @JsonProperty("enquiryPeriod")
    @Valid
    private Period enquiryPeriod;
    @JsonProperty("hasEnquiries")
    private final Boolean hasEnquiries;
    @JsonProperty("enquiries")
    private List<PsPqEnquiry> enquiries;
    @JsonProperty("amendments")
    @Valid
    private final List<Amendment> amendments;
    @JsonProperty("value")
    @Valid
    private final Value value;
    @JsonProperty("lots")
    @Valid
    private List<Lot> lots;
    @JsonProperty("lotGroups")
    @Valid
    private final List<LotGroup> lotGroups;
    @JsonProperty("items")
    @JsonDeserialize(as = LinkedHashSet.class)
    @Valid
    private final Set<Item> items;
    @JsonProperty("awardCriteria")
    private final AwardCriteria awardCriteria;
    @JsonProperty("requiresElectronicCatalogue")
    private final Boolean requiresElectronicCatalogue;
    @JsonProperty("submissionMethod")
    private final List<SubmissionMethod> submissionMethod;
    @JsonProperty("submissionMethodDetails")
    private final String submissionMethodDetails;
    @JsonProperty("submissionMethodRationale")
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
        this.hasEnquiries = hasEnquiries == null ? false : hasEnquiries;
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
