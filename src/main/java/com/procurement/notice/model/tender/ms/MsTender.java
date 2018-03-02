package com.procurement.notice.model.tender.ms;

import com.fasterxml.jackson.annotation.*;
import com.procurement.notice.model.ocds.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        "classification",
        "status",
        "statusDetails",
        "value",
        "procurementMethod",
        "procurementMethodDetails",
        "procurementMethodRationale",
        "procurementMethodAdditionalInfo",
        "mainProcurementCategory",
        "additionalProcurementCategories",
        "hasEnquiries",
        "eligibilityCriteria",
        "submissionLanguages",
        "amendments",
        "contractPeriod",
        "acceleratedProcedure",
        "designContest",
        "electronicWorkflows",
        "jointProcurement",
        "procedureOutsourcing",
        "framework",
        "dynamicPurchasingSystem",
        "legalBasis",
        "procuringEntity"
})
public class MsTender {
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

    @JsonProperty("value")
    @Valid
    private final Value value;

    @JsonProperty("procurementMethod")
    @JsonPropertyDescription("Specify tendering method using the [method codelist](http://standard.open-contracting" +
            ".org/latest/en/schema/codelists/#method). This is a closed codelist. Local method types should be mapped" +
            " to " +
            "this list.")
    @Valid
    private final ProcurementMethod procurementMethod;

    @JsonProperty("procurementMethodDetails")
    @JsonPropertyDescription("Additional detail on the procurement method used. This field may be used to provide the" +
            " local name of the particular procurement method used.")
    private final String procurementMethodDetails;

    @JsonProperty("procurementMethodRationale")
    @JsonPropertyDescription("Rationale for the chosen procurement method. This is especially important to provide a " +
            "justification in the case of limited tenders or direct awards.")
    private final String procurementMethodRationale;

    @JsonProperty("mainProcurementCategory")
    @JsonPropertyDescription("The primary category describing the main object of this contracting process from the " +
            "[procuremen     this.relatedProcesses = relatedProcesses == null ? new LinkedHashSet<>() : relatedProcesses;tCategory](http://standard.open-contracting" +
            ".org/latest/en/schema/codelists/#procurement-category)" +
            " codelist. This is a closed codelist. Local classifications should be mapped to this list.")
    @Valid
    private final MainProcurementCategory mainProcurementCategory;

    @JsonProperty("additionalProcurementCategories")
    @JsonPropertyDescription("Any additional categories which describe the objects of this contracting process, from " +
            "the [extendedProcurementCategory](http://standard.open-contracting" +
            ".org/latest/en/schema/codelists/#extended-procurement-category) codelist. This is an open codelist. " +
            "Local " +
            "categories can be included in this list.")
    private final List<ExtendedProcurementCategory> additionalProcurementCategories;

    @JsonProperty("hasEnquiries")
    @JsonPropertyDescription("A true/false field to indicate whether any enquiries were received during the tender " +
            "process. Structured information on enquiries that were received, and responses to them, can be provided " +
            "using the enquiries extension.")
    private final Boolean hasEnquiries;

    @JsonProperty("eligibilityCriteria")
    @JsonPropertyDescription("A description of any eligibility criteria for potential suppliers.")
    private final String eligibilityCriteria;

    @JsonProperty("contractPeriod")
    @Valid
    private final Period contractPeriod;

    @JsonProperty("procuringEntity")
    @JsonPropertyDescription("The id and name of the party being referenced. Used to cross-reference to the parties " +
            "section")
    @Valid
    private final OrganizationReference procuringEntity;

    @JsonProperty("amendments")
    @JsonPropertyDescription("A tender amendment is a formal change to the tender, and generally involves the " +
            "publication of a new tender notice/release. The rationale and a description of the changes made can be " +
            "provided here.")
    @Valid
    private final List<Amendment> amendments;

    @JsonProperty("acceleratedProcedure")
    @Valid
    private final AcceleratedProcedure acceleratedProcedure;

    @JsonProperty("classification")
    @Valid
    private final Classification classification;

    @JsonProperty("designContest")
    @Valid
    private final DesignContest designContest;

    @JsonProperty("electronicWorkflows")
    @Valid
    private final ElectronicWorkflows electronicWorkflows;

    @JsonProperty("jointProcurement")
    @Valid
    private final JointProcurement jointProcurement;

    @JsonProperty("legalBasis")
    @JsonPropertyDescription("The legal basis of the tender based on the [legalBasis codelist](http://standard" +
            ".open-contracting.org/......")
    private final LegalBasis legalBasis;

    @JsonProperty("procedureOutsourcing")
    @Valid
    private final ProcedureOutsourcing procedureOutsourcing;

    @JsonProperty("procurementMethodAdditionalInfo")
    @JsonPropertyDescription("Additional information about the procurement method.")
    private final String procurementMethodAdditionalInfo;

    @JsonProperty("submissionLanguages")
    @JsonPropertyDescription("Language(s) in which tenderers may submit, drawn from the [submissionLanguages " +
            "codelist](http://standard.open-contracting.org/1.1-dev/en/schema/codelists/#submission-languages)")
    private final List<SubmissionLanguage> submissionLanguages;

    @JsonProperty("dynamicPurchasingSystem")
    @JsonPropertyDescription("Dynamic Purchasing System: Whether a dynamic purchasing system has been set up and if " +
            "so whether it may be used by buyers outside the notice. Required by EU.")
    @Valid
    private final DynamicPurchasingSystem dynamicPurchasingSystem;

    @JsonProperty("framework")
    @JsonPropertyDescription("The details of any framework agreement established as part of this procurement. " +
            "Required by EU.")
    @Valid
    private final Framework framework;

    @JsonCreator
    public MsTender(@JsonProperty("id") final String id,
                    @JsonProperty("title") final String title,
                    @JsonProperty("description") final String description,
                    @JsonProperty("status") final TenderStatus status,
                    @JsonProperty("statusDetails") final TenderStatusDetails statusDetails,
                    @JsonProperty("value") final Value value,
                    @JsonProperty("procurementMethod") final ProcurementMethod procurementMethod,
                    @JsonProperty("procurementMethodDetails") final String procurementMethodDetails,
                    @JsonProperty("procurementMethodRationale") final String procurementMethodRationale,
                    @JsonProperty("mainProcurementCategory") final MainProcurementCategory mainProcurementCategory,
                    @JsonProperty("additionalProcurementCategories") final List<ExtendedProcurementCategory> additionalProcurementCategories,
                    @JsonProperty("hasEnquiries") final Boolean hasEnquiries,
                    @JsonProperty("eligibilityCriteria") final String eligibilityCriteria,
                    @JsonProperty("contractPeriod") final Period contractPeriod,
                    @JsonProperty("procuringEntity") final OrganizationReference procuringEntity,
                    @JsonProperty("amendments") final List<Amendment> amendments,
                    @JsonProperty("acceleratedProcedure") final AcceleratedProcedure acceleratedProcedure,
                    @JsonProperty("classification") final Classification classification,
                    @JsonProperty("designContest") final DesignContest designContest,
                    @JsonProperty("electronicWorkflows") final ElectronicWorkflows electronicWorkflows,
                    @JsonProperty("jointProcurement") final JointProcurement jointProcurement,
                    @JsonProperty("legalBasis") final LegalBasis legalBasis,
                    @JsonProperty("procedureOutsourcing") final ProcedureOutsourcing procedureOutsourcing,
                    @JsonProperty("procurementMethodAdditionalInfo") final String procurementMethodAdditionalInfo,
                    @JsonProperty("submissionLanguages") final List<SubmissionLanguage> submissionLanguages,
                    @JsonProperty("dynamicPurchasingSystem") final DynamicPurchasingSystem dynamicPurchasingSystem,
                    @JsonProperty("framework") final Framework framework) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.statusDetails = statusDetails;
        this.value = value;
        this.procurementMethod = procurementMethod;
        this.procurementMethodDetails = procurementMethodDetails;
        this.procurementMethodRationale = procurementMethodRationale;
        this.mainProcurementCategory = mainProcurementCategory;
        this.additionalProcurementCategories = additionalProcurementCategories;
        this.hasEnquiries = hasEnquiries;
        this.eligibilityCriteria = eligibilityCriteria;
        this.contractPeriod = contractPeriod;
        this.procuringEntity = procuringEntity;
        this.amendments = amendments;
        this.acceleratedProcedure = acceleratedProcedure;
        this.classification = classification;
        this.designContest = designContest;
        this.electronicWorkflows = electronicWorkflows;
        this.jointProcurement = jointProcurement;
        this.legalBasis = legalBasis;
        this.procedureOutsourcing = procedureOutsourcing;
        this.procurementMethodAdditionalInfo = procurementMethodAdditionalInfo;
        this.submissionLanguages = submissionLanguages;
        this.dynamicPurchasingSystem = dynamicPurchasingSystem;
        this.framework = framework;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(title)
                .append(description)
                .append(status)
                .append(statusDetails)
                .append(value)
                .append(procurementMethod)
                .append(procurementMethodDetails)
                .append(procurementMethodRationale)
                .append(mainProcurementCategory)
                .append(additionalProcurementCategories)
                .append(hasEnquiries)
                .append(eligibilityCriteria)
                .append(contractPeriod)
                .append(procuringEntity)
                .append(amendments)
                .append(acceleratedProcedure)
                .append(classification)
                .append(designContest)
                .append(electronicWorkflows)
                .append(jointProcurement)
                .append(legalBasis)
                .append(procedureOutsourcing)
                .append(procurementMethodAdditionalInfo)
                .append(submissionLanguages)
                .append(dynamicPurchasingSystem)
                .append(framework)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MsTender)) {
            return false;
        }
        final MsTender rhs = (MsTender) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(title, rhs.title)
                .append(description, rhs.description)
                .append(status, rhs.status)
                .append(statusDetails, rhs.statusDetails)
                .append(value, rhs.value)
                .append(procurementMethod, rhs.procurementMethod)
                .append(procurementMethodDetails, rhs.procurementMethodDetails)
                .append(procurementMethodRationale, rhs.procurementMethodRationale)
                .append(mainProcurementCategory, rhs.mainProcurementCategory)
                .append(additionalProcurementCategories, rhs.additionalProcurementCategories)
                .append(hasEnquiries, rhs.hasEnquiries)
                .append(eligibilityCriteria, rhs.eligibilityCriteria)
                .append(contractPeriod, rhs.contractPeriod)
                .append(procuringEntity, rhs.procuringEntity)
                .append(amendments, rhs.amendments)
                .append(acceleratedProcedure, rhs.acceleratedProcedure)
                .append(classification, rhs.classification)
                .append(designContest, rhs.designContest)
                .append(electronicWorkflows, rhs.electronicWorkflows)
                .append(jointProcurement, rhs.jointProcurement)
                .append(legalBasis, rhs.legalBasis)
                .append(procedureOutsourcing, rhs.procedureOutsourcing)
                .append(procurementMethodAdditionalInfo, rhs.procurementMethodAdditionalInfo)
                .append(submissionLanguages, rhs.submissionLanguages)
                .append(dynamicPurchasingSystem, rhs.dynamicPurchasingSystem)
                .append(framework, rhs.framework)
                .isEquals();
    }

    public enum MainProcurementCategory {
        GOODS("goods"),
        WORKS("works"),
        SERVICES("services");

        private final static Map<String, MainProcurementCategory> CONSTANTS = new HashMap<>();
        private final String value;

        static {
            for (final MainProcurementCategory c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MainProcurementCategory(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static MainProcurementCategory fromValue(final String value) {
            final MainProcurementCategory constant = CONSTANTS.get(value);
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

    public enum ProcurementMethod {
        OPEN("open"),
        SELECTIVE("selective"),
        LIMITED("limited"),
        DIRECT("direct");

        private final static Map<String, ProcurementMethod> CONSTANTS = new HashMap<>();
        private final String value;

        static {
            for (final ProcurementMethod c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ProcurementMethod(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static ProcurementMethod fromValue(final String value) {
            final ProcurementMethod constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            }
            return constant;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }


    }

    public enum ExtendedProcurementCategory {
        GOODS("goods"),
        WORKS("works"),
        SERVICES("services"),
        CONSULTING_SERVICES("consultingServices");

        private final static Map<String, ExtendedProcurementCategory> CONSTANTS = new HashMap<>();
        private final String value;

        static {
            for (final ExtendedProcurementCategory c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ExtendedProcurementCategory(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static ExtendedProcurementCategory fromValue(final String value) {
            final ExtendedProcurementCategory constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            }
            return constant;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum LegalBasis {
        DIRECTIVE_2014_23_EU("DIRECTIVE_2014_23_EU"),
        DIRECTIVE_2014_24_EU("DIRECTIVE_2014_24_EU"),
        DIRECTIVE_2014_25_EU("DIRECTIVE_2014_25_EU"),
        DIRECTIVE_2009_81_EC("DIRECTIVE_2009_81_EC"),
        REGULATION_966_2012("REGULATION_966_2012"),
        NATIONAL_PROCUREMENT_LAW("NATIONAL_PROCUREMENT_LAW"),
        NULL("NULL");

        private final static Map<String, LegalBasis> CONSTANTS = new HashMap<>();
        private final String value;

        static {
            for (final LegalBasis c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LegalBasis(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static LegalBasis fromValue(final String value) {
            final LegalBasis constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            }
            return constant;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum SubmissionLanguage {
        BG("bg"),
        ES("es"),
        CS("cs"),
        DA("da"),
        DE("de"),
        ET("et"),
        EL("el"),
        EN("en"),
        FR("fr"),
        GA("ga"),
        HR("hr"),
        IT("it"),
        LV("lv"),
        LT("lt"),
        HU("hu"),
        MT("mt"),
        NL("nl"),
        PL("pl"),
        PT("pt"),
        RO("ro"),
        SK("sk"),
        SL("sl"),
        FI("fi"),
        SV("sv");

        private final static Map<String, SubmissionLanguage> CONSTANTS = new HashMap<>();
        private final String value;

        static {
            for (final SubmissionLanguage c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SubmissionLanguage(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static SubmissionLanguage fromValue(final String value) {
            final SubmissionLanguage constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            }
            return constant;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
