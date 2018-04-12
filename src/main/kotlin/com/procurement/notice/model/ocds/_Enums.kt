package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException
import java.util.*

enum class AwardCriteria constructor(val value: String) {
    PRICE_ONLY("priceOnly"),
    COST_ONLY("costOnly"),
    QUALITY_ONLY("qualityOnly"),
    RATED_CRITERIA("ratedCriteria"),
    LOWEST_COST("lowestCost"),
    BEST_PROPOSAL("bestProposal"),
    BEST_VALUE_TO_GOVERNMENT("bestValueToGovernment"),
    SINGLE_BID_ONLY("singleBidOnly");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, AwardCriteria>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): AwardCriteria {
            return CONSTANTS[value]
                    ?: throw EnumException(AwardCriteria::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class AwardStatus constructor(private val value: String) {

    PENDING("pending"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    EMPTY("empty");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, AwardStatus>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): AwardStatus {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class BidStatus constructor(private val value: String) {

    INVITED("invited"),
    PENDING("pending"),
    VALID("valid"),
    DISQUALIFIED("disqualified"),
    WITHDRAWN("withdrawn");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, BidStatus>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): BidStatus {
            return CONSTANTS[value] ?: throw IllegalArgumentException(
                    "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()))
        }
    }
}

enum class BidStatusDetails constructor(private val value: String) {
    VALID("valid"),
    DISQUALIFIED("disqualified"),
    EMPTY("empty");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, BidStatusDetails>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): BidStatusDetails {
            return CONSTANTS[value] ?: throw IllegalArgumentException(
                    "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()))
        }
    }
}

enum class ContractStatus constructor(private val value: String) {
    PENDING("pending"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    TERMINATED("terminated");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, ContractStatus>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ContractStatus {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class DataType constructor(private val value: String) {
    STRING("string"),
    DATE_TIME("date-time"),
    NUMBER("number"),
    INTEGER("integer");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, DataType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): DataType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class DocumentType private constructor(private val value: String) {
    TENDER_NOTICE("tenderNotice"),
    AWARD_NOTICE("awardNotice"),
    CONTRACT_NOTICE("contractNotice"),
    COMPLETION_CERTIFICATE("completionCertificate"),
    PROCUREMENT_PLAN("procurementPlan"),
    BIDDING_DOCUMENTS("biddingDocuments"),
    TECHNICAL_SPECIFICATIONS("technicalSpecifications"),
    EVALUATION_CRITERIA("evaluationCriteria"),
    EVALUATION_REPORTS("evaluationReports"),
    CONTRACT_DRAFT("contractDraft"),
    CONTRACT_SIGNED("contractSigned"),
    CONTRACT_ARRANGEMENTS("contractArrangements"),
    CONTRACT_SCHEDULE("contractSchedule"),
    PHYSICAL_PROGRESS_REPORT("physicalProgressReport"),
    FINANCIAL_PROGRESS_REPORT("financialProgressReport"),
    FINAL_AUDIT("finalAudit"),
    HEARING_NOTICE("hearingNotice"),
    MARKET_STUDIES("marketStudies"),
    ELIGIBILITY_CRITERIA("eligibilityCriteria"),
    CLARIFICATIONS("clarifications"),
    SHORTLISTED_FIRMS("shortlistedFirms"),
    ENVIRONMENTAL_IMPACT("environmentalImpact"),
    ASSET_AND_LIABILITY_ASSESSMENT("assetAndLiabilityAssessment"),
    RISK_PROVISIONS("riskProvisions"),
    WINNING_BID("winningBid"),
    COMPLAINTS("complaints"),
    CONTRACT_ANNEXE("contractAnnexe"),
    CONTRACT_GUARANTEES("contractGuarantees"),
    SUB_CONTRACT("subContract"),
    NEEDS_ASSESSMENT("needsAssessment"),
    FEASIBILITY_STUDY("feasibilityStudy"),
    PROJECT_PLAN("projectPlan"),
    BILL_OF_QUANTITY("billOfQuantity"),
    BIDDERS("bidders"),
    CONFLICT_OF_INTEREST("conflictOfInterest"),
    DEBARMENTS("debarments"),
    ILLUSTRATION("illustration"),
    SUBMISSION_DOCUMENTS("submissionDocuments"),
    CONTRACT_SUMMARY("contractSummary"),
    CANCELLATION_DETAILS("cancellationDetails");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, DocumentType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): DocumentType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class ExtendedProcurementCategory constructor(val value: String) {
    GOODS("goods"),
    WORKS("works"),
    SERVICES("services"),
    CONSULTING_SERVICES("consultingServices");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, ExtendedProcurementCategory>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ExtendedProcurementCategory {
            return CONSTANTS[value]
                    ?: throw EnumException(ExtendedProcurementCategory::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class ValueBreakdownType constructor(private val value: String) {
    USER_FEES("userFees"),
    PUBLIC_AGENCY_FEES("publicAgencyFees");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, ValueBreakdownType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ValueBreakdownType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class UnitScheme constructor(private val value: String) {
    UNCEFACT("UNCEFACT"),
    QUDT("QUDT");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, UnitScheme>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): UnitScheme {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class MainGeneralActivity constructor(private val value: String) {
    DEFENCE("DEFENCE"),
    ECONOMIC_AND_FINANCIAL_AFFAIRS("ECONOMIC_AND_FINANCIAL_AFFAIRS"),
    EDUCATION("EDUCATION"),
    ENVIRONMENT("ENVIRONMENT"),
    GENERAL_PUBLIC_SERVICES("GENERAL_PUBLIC_SERVICES"),
    HEALTH("HEALTH"),
    HOUSING_AND_COMMUNITY_AMENITIES("HOUSING_AND_COMMUNITY_AMENITIES"),
    PUBLIC_ORDER_AND_SAFETY("PUBLIC_ORDER_AND_SAFETY"),
    RECREATION_CULTURE_AND_RELIGION("RECREATION_CULTURE_AND_RELIGION"),
    SOCIAL_PROTECTION("SOCIAL_PROTECTION");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, MainGeneralActivity>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): MainGeneralActivity {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class MainSectoralActivity constructor(private val value: String) {
    AIRPORT_RELATED_ACTIVITIES("AIRPORT_RELATED_ACTIVITIES"),
    ELECTRICITY("ELECTRICITY"),
    EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL("EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL"),
    EXPLORATION_EXTRACTION_GAS_OIL("EXPLORATION_EXTRACTION_GAS_OIL"),
    PORT_RELATED_ACTIVITIES("PORT_RELATED_ACTIVITIES"),
    POSTAL_SERVICES("POSTAL_SERVICES"),
    PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT("PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT"),
    RAILWAY_SERVICES("RAILWAY_SERVICES"),
    URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES("URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES"),
    WATER("WATER");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, MainSectoralActivity>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): MainSectoralActivity {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class Scale constructor(private val value: String) {
    MICRO("micro"),
    SME("sme"),
    LARGE("large"),
    EMPTY("");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, Scale>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Scale {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class TypeOfBuyer constructor(private val value: String) {
    BODY_PUBLIC("BODY_PUBLIC"),
    EU_INSTITUTION("EU_INSTITUTION"),
    MINISTRY("MINISTRY"),
    NATIONAL_AGENCY("NATIONAL_AGENCY"),
    REGIONAL_AGENCY("REGIONAL_AGENCY"),
    REGIONAL_AUTHORITY("REGIONAL_AUTHORITY");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, TypeOfBuyer>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TypeOfBuyer {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class InitiationType constructor(private val value: String) {

    TENDER("tender");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, InitiationType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): InitiationType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class LegalBasis constructor(val value: String) {
    DIRECTIVE_2014_23_EU("DIRECTIVE_2014_23_EU"),
    DIRECTIVE_2014_24_EU("DIRECTIVE_2014_24_EU"),
    DIRECTIVE_2014_25_EU("DIRECTIVE_2014_25_EU"),
    DIRECTIVE_2009_81_EC("DIRECTIVE_2009_81_EC"),
    REGULATION_966_2012("REGULATION_966_2012"),
    NATIONAL_PROCUREMENT_LAW("NATIONAL_PROCUREMENT_LAW"),
    NULL("NULL");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, LegalBasis>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): LegalBasis {
            return CONSTANTS[value]
                    ?: throw EnumException(LegalBasis::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class MainProcurementCategory constructor(val value: String) {
    GOODS("goods"),
    WORKS("works"),
    SERVICES("services");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, MainProcurementCategory>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): MainProcurementCategory {
            return CONSTANTS[value]
                    ?: throw EnumException(MainProcurementCategory::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class Measure private constructor(private val value: String) {
    REQUESTS("requests"),
    BIDS("bids"),
    VALID_BIDS("validBids"),
    BIDDERS("bidders"),
    QUALIFIED_BIDDERS("qualifiedBidders"),
    DISQUALIFIED_BIDDERS("disqualifiedBidders"),
    ELECTRONIC_BIDS("electronicBids"),
    SME_BIDS("smeBids"),
    FOREIGN_BIDS("foreignBids"),
    FOREIGN_BIDS_FROM_EU("foreignBidsFromEU"),
    TENDERS_ABNORMALLY_LOW("tendersAbnormallyLow");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, Measure>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Measure {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class MilestoneStatus constructor(private val value: String) {
    SCHEDULED("scheduled"),
    MET("met"),
    NOT_MET("notMet"),
    PARTIALLY_MET("partiallyMet");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, MilestoneStatus>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): MilestoneStatus {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class MilestoneType constructor(private val value: String) {
    PRE_PROCUREMENT("preProcurement"),
    APPROVAL("approval"),
    ENGAGEMENT("engagement"),
    ASSESSMENT("assessment"),
    DELIVERY("delivery"),
    REPORTING("reporting"),
    FINANCING("financing");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, MilestoneType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): MilestoneType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class ObjectivesType constructor(private val value: String) {
    ENVIRONMENTAL("environmental"),
    SOCIAL("social"),
    INNOVATIVE("innovative");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, ObjectivesType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ObjectivesType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class PartyRole constructor(private val value: String) {
    BUYER("buyer"),
    PROCURING_ENTITY("procuringEntity"),
    SUPPLIER("supplier"),
    TENDERER("tenderer"),
    FUNDER("funder"),
    ENQUIRER("enquirer"),
    PAYER("payer"),
    PAYEE("payee"),
    REVIEW_BODY("reviewBody");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, PartyRole>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): PartyRole {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class ParticipationFeeType constructor(private val value: String) {
    DOCUMENT("document"),
    DEPOSIT("deposit"),
    SUBMISSION("submission"),
    WIN("win");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, ParticipationFeeType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ParticipationFeeType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class ProcurementMethod constructor(val value: String) {
    OPEN("open"),
    SELECTIVE("selective"),
    LIMITED("limited"),
    DIRECT("direct");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, ProcurementMethod>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ProcurementMethod {
            return CONSTANTS[value]
                    ?: throw EnumException(ProcurementMethod::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class Relationship constructor(private val value: String) {
    PREVIOUS_NOTICE("previousNotice");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, Relationship>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Relationship {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class RelatedNoticeScheme constructor(private val value: String) {
    TED("TED"),
    NATIONAL("National");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, RelatedNoticeScheme>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): RelatedNoticeScheme {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class RelatedProcessScheme constructor(private val value: String) {

    OCID("ocid");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, RelatedProcessScheme>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): RelatedProcessScheme {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}


enum class RelatedProcessType constructor(private val value: String) {
    FRAMEWORK("framework"),
    PLANNING("planning"),
    PARENT("parent"),
    PRIOR("prior"),
    UNSUCCESSFUL_PROCESS("unsuccessfulProcess"),
    REPLACEMENT_PROCESS("replacementProcess"),
    RENEWAL_PROCESS("renewalProcess"),
    SUB_CONTRACT("subContract"),
    X_EXPENDITURE_ITEM("x_expenditureItem"),
    X_FINANCE_SOURCE("x_financeSource"),
    X_PRESELECTION("x_preselection"),
    X_PREQUALIFICATION("x_prequalification"),
    X_EVALUATION("x_evaluation"),
    X_EXECUTION("x_execution"),
    X_PLANNED("x_planned"),
    X_BUDGET("x_budget");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, RelatedProcessType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): RelatedProcessType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class RelatesTo constructor(private val value: String) {
    ITEM("item"),
    TENDERER("tenderer");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, RelatesTo>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): RelatesTo {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class Source constructor(private val value: String) {
    TENDERER("tenderer"),
    BUYER("buyer"),
    PROCURING_ENTITY("procuringEntity");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, Source>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Source {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class SubmissionMethod constructor(val value: String) {
    ELECTRONIC_SUBMISSION("electronicSubmission"),
    ELECTRONIC_AUCTION("electronicAuction"),
    WRITTEN("written"),
    IN_PERSON("inPerson");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, SubmissionMethod>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): SubmissionMethod {
            return CONSTANTS[value]
                    ?: throw EnumException(SubmissionMethod::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class ClassificationScheme constructor(private val value: String) {
    CPV("CPV"),
    CPVS("CPVS"),
    GSIN("GSIN"),
    UNSPSC("UNSPSC"),
    CPC("CPC"),
    OKDP("OKDP"),
    OKPD("OKPD");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, ClassificationScheme>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ClassificationScheme {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class SubmissionMethodRationale constructor(val value: String) {
    TOOLS_DEVICES_FILE_FORMATS_UNAVAILABLE("TOOLS_DEVICES_FILE_FORMATS_UNAVAILABLE"),
    IPR_ISSUES("IPR_ISSUES"),
    REQUIRES_SPECIALISED_EQUIPMENT("REQUIRES_SPECIALISED_EQUIPMENT"),
    PHYSICAL_MODEL("PHYSICAL_MODEL"),
    SENSITIVE_INFORMATION("SENSITIVE_INFORMATION");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, SubmissionMethodRationale>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): SubmissionMethodRationale {
            return CONSTANTS[value]
                    ?: throw EnumException(SubmissionMethodRationale::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class SubmissionLanguage constructor(val value: String) {
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

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {

        val CONSTANTS = HashMap<String, SubmissionLanguage>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): SubmissionLanguage {
            return CONSTANTS[value]
                    ?: throw EnumException(SubmissionLanguage::class.java.name, value, Arrays.toString(values()))
        }
    }
}


enum class Tag constructor(private val value: String) {
    PLANNING("planning"),
    PLANNING_UPDATE("planningUpdate"),
    TENDER("tender"),
    TENDER_AMENDMENT("tenderAmendment"),
    TENDER_UPDATE("tenderUpdate"),
    TENDER_CANCELLATION("tenderCancellation"),
    AWARD("award"),
    AWARD_UPDATE("awardUpdate"),
    AWARD_CANCELLATION("awardCancellation"),
    CONTRACT("contract"),
    CONTRACT_UPDATE("contractUpdate"),
    CONTRACT_AMENDMENT("contractAmendment"),
    IMPLEMENTATION("implementation"),
    IMPLEMENTATION_UPDATE("implementationUpdate"),
    CONTRACT_TERMINATION("contractTermination"),
    COMPILED("compiled");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, Tag>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Tag {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class TypeOfFramework constructor(private val value: String) {
    WITH_REOPENING_OF_COMPETITION("WITH_REOPENING_OF_COMPETITION"),
    WITHOUT_REOPENING_OF_COMPETITION("WITHOUT_REOPENING_OF_COMPETITION"),
    PARTLY_WITH_PARTLY_WITHOUT_REOPENING_OF_COMPETITION("PARTLY_WITH_PARTLY_WITHOUT_REOPENING_OF_COMPETITION");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, TypeOfFramework>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TypeOfFramework {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class TenderStatus constructor(private val value: String) {
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete"),
    WITHDRAWN("withdrawn");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, TenderStatus>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TenderStatus {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class TenderStatusDetails constructor(private val value: String) {
    PRESELECTION("preselection"),
    PRESELECTED("preselected"),
    PREQUALIFICATION("prequalification"),
    PREQUALIFIED("prequalified"),
    PLANNING_NOTICE("planning notice"),
    PRIOR_NOTICE("prior notice"),
    EVALUATION("evaluation"),
    EVALUATED("evaluated"),
    EXECUTION("execution"),
    //**//
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    BLOCKED("blocked"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete"),
    WITHDRAWN("withdrawn"),
    SUSPENDED("suspended"),
    EMPTY("empty");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, TenderStatusDetails>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TenderStatusDetails {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}