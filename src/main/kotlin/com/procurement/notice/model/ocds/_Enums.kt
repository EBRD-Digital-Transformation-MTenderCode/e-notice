package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

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

enum class PartyRole constructor(private val value: String) {
    BUYER("buyer"),
    CANDIDATE("candidate"),
    ENQUIRER("enquirer"),
    FUNDER("funder"),
    INVITED_TENDERER("invitedTenderer"),
    PAYEE("payee"),
    PAYER("payer"),
    PROCURING_ENTITY("procuringEntity"),
    REVIEW_BODY("reviewBody"),
    SUPPLIER("supplier"),
    TENDERER("tenderer");

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

    AGGREGATE_PLANNING("aggregatePlanning"),
    FRAMEWORK("framework"),
    PARENT("parent"),
    PLANNING("planning"),
    X_CONTRACTING("x_contracting"),
    X_DEMAND("x_demand"),
    X_ESTABLISHMENT("x_establishment"),
    X_EVALUATION("x_evaluation"),
    X_EXECUTION("x_execution"),
    X_EXPENDITURE_ITEM("x_expenditureItem"),
    X_FUNDING_SOURCE("x_fundingSource"),
    X_NEGOTIATION("x_negotiation"),
    X_PLANNED("x_planned"),
    X_PREQUALIFICATION("x_prequalification"),
    X_PRESELECTION("x_preselection"),
    X_SCOPE("x_scope"),
    X_TENDERING("x_tendering");

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

enum class TenderDescription(val text: String) {

    PS("Preselection stage of contracting process"),
    PQ("Prequalification stage of contracting process"),
    EV("Evaluation stage of contracting process"),
    PN("Contracting process is planned"),
    PIN("Date of tender launch is determined"),
    NP("Negotiation stage of contracting process"),
    TP("Tendering Phase stage of contracting process")
}

enum class TenderTitle(val text: String) {

    PS("Preselection"),
    PQ("Prequalification"),
    EV("Evaluation"),
    PN("Planning Notice"),
    PIN("Prior Notice"),
    NP("Negotiation"),
    TP("Tendering Phase")
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
    AGGREGATED("aggregated"),
    AGGREGATE_PLANNING("aggregatePlanning"),
    AGGREGATION("aggregation"),
    AGGREGATION_PENDING("aggregationPending"),
    AUCTION("auction"),
    AWARDED("awarded"),
    AWARDED_CONTRACT_PREPARATION("awardedContractPreparation"),
    AWARDED_STANDSTILL("awardedStandStill"),
    AWARDED_SUSPENDED("awardedSuspended"),
    AWARDING("awarding"),
    BLOCKED("blocked"),
    CANCELLATION("cancellation"),
    CANCELLED("cancelled"),
    CLARIFICATION("clarification"),
    COMPLETE("complete"),
    EMPTY("empty"),
    ESTABLISHMENT("establishment"),
    EVALUATED("evaluated"),
    EVALUATION("evaluation"),
    EXECUTION("execution"),
    LACK_OF_QUALIFICATIONS("lackOfQualifications"),
    LACK_OF_SUBMISSIONS("lackOfSubmissions"),
    NEGOTIATION("negotiation"),
    PLANNED("planned"),
    PLANNING("planning"),
    PLANNING_NOTICE("planning notice"),
    PREQUALIFICATION("prequalification"),
    PREQUALIFIED("prequalified"),
    PRESELECTED("preselected"),
    PRESELECTION("preselection"),
    PRIOR_NOTICE("prior notice"),
    QUALIFICATION("qualification"),
    QUALIFICATION_STAND_STILL("qualificationStandStill"),
    STANDSTILL("standStill"),
    SUBMISSION("submission"),
    SUSPENDED("suspended"),
    TENDERING("tendering"),
    UNSUCCESSFUL("unsuccessful"),
    WITHDRAWN("withdrawn");

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

enum class LotStatus constructor(private val value: String) {
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, LotStatus>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): LotStatus {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class LotStatusDetails constructor(private val value: String) {
    UNSUCCESSFUL("unsuccessful"),
    AWARDED("awarded"),
    CANCELLED("cancelled"),
    EMPTY("empty");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, LotStatusDetails>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): LotStatusDetails {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}

enum class Stage {
    AP,
    CT,
    EI,
    EV,
    FE,
    FS,
    NP,
    PIN,
    PN,
    PQ,
    PS,
    TP
}

enum class Operation(val value: String) {
    CREATE_EI("createEI"),
    UPDATE_EI("updateEI"),
    CREATE_FS("createFS"),
    UPDATE_FS("updateFS"),
    CREATE_CN("createCN"),
    CREATE_PN("createPN"),
    CREATE_AP("createAP"),
    CREATE_PIN("createPIN"),
    UPDATE_CN("updateCN"),
    UPDATE_PN("updatePN"),
    UPDATE_AP("updateAP"),
    UPDATE_TENDER_PERIOD("updateTenderPeriod"),
    CREATE_ENQUIRY("createEnquiry"),
    ADD_ANSWER("addAnswer"),
    UNSUSPEND_TENDER("unsuspendTender"),
    UNSUCCESSFUL_TENDER("tenderUnsuccessful"),
    @Deprecated(message = "Using 'tenderPeriodEndEv'.", level = DeprecationLevel.WARNING)
    TENDER_PERIOD_END("tenderPeriodEnd"),
    TENDER_PERIOD_END_EV("tenderPeriodEndEv"),
    TENDER_PERIOD_END_AUCTION("tenderPeriodEndAuction"),
    AUCTION_PERIOD_END("auctionPeriodEnd"),
    ENQUIRY_PERIOD_END("enquiryPeriodEnd"),
    SUSPEND_TENDER("suspendTender"),
    AWARD_BY_BID("awardByBid"),
    AWARD_BY_BID_EV("awardByBidEv"),
    AWARD_PERIOD_END("awardPeriodEnd"),
    STANDSTILL_PERIOD("standstillPeriod"),
    START_NEW_STAGE("startNewStage"),
    CREATE_PIN_ON_PN("createPINonPN"),
    CREATE_CN_ON_PN("createCNonPN"),
    CREATE_NEGOTIATION_CN_ON_PN("createNegotiationCnOnPn"),
    CREATE_CN_ON_PIN("createCNonPIN"),
    CREATE_FE("createFE"),
    CANCEL_TENDER("cancelTender"),
    CANCEL_TENDER_EV("cancelTenderEv"),
    CANCEL_PLAN("cancelPlan"),
    CANCEL_STANDSTILL_PERIOD("cancellationStandstillPeriod"),
    UPDATE_BID_DOCS("updateBidDocs"),
    CREATE_AC("createAC"),
    UPDATE_AC("updateAC"),
    ISSUING_AC("issuingAC"),
    FINAL_UPDATE("finalUpdateAC"),
    BUYER_SIGNING_AC("buyerSigningAC"),
    SUPPLIER_SIGNING_AC("supplierSigningAC"),
    VERIFICATION_AC("verificationAC"),
    TREASURY_APPROVING_AC("treasuryApprovingAC"),
    ACTIVATION_AC("activationAC"),
    END_AWARD_PERIOD("endAwardPeriod"),
    CREATE_PROTOCOL("createProtocol"),
    CREATE_CAN("createCan"),
    UPDATE_CAN_DOCS("updateCanDocs"),
    CANCEL_CAN("cancelCan"),
    CANCEL_CAN_CONTRACT("cancelCanContract"),
    CONFIRM_CAN("confirmCan"),
    END_CONTRACT_PROCESS("endContractingProcess"),
    CREATE_AWARD("createAward"),
    START_AWARD_PERIOD("startAwardPeriod"),
    EVALUATE_AWARD("evaluateAward"),
    PROCESS_AC_CLARIFICATION("processAcClarification"),
    PROCESS_AC_REJECTION("processAcRejection"),
    DO_AWARD_CONSIDERATION("doAwardConsideration");


    companion object {
        private val CONSTANTS = HashMap<String, Operation>()

        init {
            for (c in Operation.values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Operation {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}
enum class DocumentTypeContract(@JsonValue val value: String) {

    CONTRACT_NOTICE("contractNotice"),
    COMPLETION_CERTIFICATE("completionCertificate"),
    CONTRACT_DRAFT("contractDraft"),
    CONTRACT_ARRANGEMENTS("contractArrangements"),
    CONTRACT_SCHEDULE("contractSchedule"),
    ENVIRONMENTAL_IMPACT("environmentalImpact"),
    CONTRACT_ANNEXE("contractAnnexe"),
    CONTRACT_GUARANTEES("contractGuarantees"),
    SUB_CONTRACT("subContract"),
    ILLUSTRATION("illustration"),
    CONTRACT_SIGNED("contractSigned"),
    CONTRACT_SUMMARY("contractSummary"),
    BUYERS_RESPONSE_ADD("buyersResponseAdd");

    override fun toString(): String {
        return this.value
    }
}