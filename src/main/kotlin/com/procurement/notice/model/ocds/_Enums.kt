package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

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
    CENTRAL_PURCHASING_BODY("centralPurchasingBody"),
    CLIENT("client"),
    ENQUIRER("enquirer"),
    FUNDER("funder"),
    INVITED_CANDIDATE("invitedCandidate"),
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
    X_CATALOGUE("x_catalogue"),
    X_CONTRACTING("x_contracting"),
    X_DEMAND("x_demand"),
    X_ESTABLISHMENT("x_establishment"),
    X_EVALUATION("x_evaluation"),
    X_EXECUTION("x_execution"),
    X_EXPENDITURE_ITEM("x_expenditureItem"),
    X_FRAMEWORK("x_framework"),
    X_FUNDING_SOURCE("x_fundingSource"),
    X_NEGOTIATION("x_negotiation"),
    X_PLANNED("x_planned"),
    X_PREQUALIFICATION("x_prequalification"),
    X_PRESELECTION("x_preselection"),
    X_PRE_AWARD_CATALOG_REQUEST("x_preAwardCatalogRequest"),
    X_PURCHASING("x_purchasing"),
    X_REQUEST_QUOTATION("x_requestQuotation"),
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
    PC,
    PIN,
    PN,
    PO,
    PQ,
    PS,
    RQ,
    TP,
}

enum class Operation(val value: String) {
    ACTIVATION_AC("activationAC"),
    ADD_ANSWER("addAnswer"),
    AMEND_FE("amendFE"),
    AUCTION_PERIOD_END("auctionPeriodEnd"),
    AWARD_BY_BID("awardByBid"),
    AWARD_BY_BID_EV("awardByBidEv"),
    AWARD_PERIOD_END("awardPeriodEnd"),
    BUYER_SIGNING_AC("buyerSigningAC"),
    CANCEL_CAN("cancelCan"),
    CANCEL_CAN_CONTRACT("cancelCanContract"),
    CANCEL_PLAN("cancelPlan"),
    CANCEL_STANDSTILL_PERIOD("cancellationStandstillPeriod"),
    CANCEL_TENDER("cancelTender"),
    CANCEL_TENDER_EV("cancelTenderEv"),
    CONFIRM_CAN("confirmCan"),
    CREATE_AC("createAC"),
    CREATE_AP("createAP"),
    CREATE_AWARD("createAward"),
    CREATE_CAN("createCan"),
    CREATE_CN("createCN"),
    CREATE_CN_ON_PIN("createCNonPIN"),
    CREATE_CN_ON_PN("createCNonPN"),
    CREATE_EI("createEI"),
    CREATE_ENQUIRY("createEnquiry"),
    CREATE_FE("createFE"),
    CREATE_FS("createFS"),
    CREATE_NEGOTIATION_CN_ON_PN("createNegotiationCnOnPn"),
    CREATE_PIN("createPIN"),
    CREATE_PIN_ON_PN("createPINonPN"),
    CREATE_PN("createPN"),
    CREATE_PROTOCOL("createProtocol"),
    DO_AWARD_CONSIDERATION("doAwardConsideration"),
    END_AWARD_PERIOD("endAwardPeriod"),
    END_CONTRACT_PROCESS("endContractingProcess"),
    ENQUIRY_PERIOD_END("enquiryPeriodEnd"),
    EVALUATE_AWARD("evaluateAward"),
    FINAL_UPDATE("finalUpdateAC"),
    ISSUING_AC("issuingAC"),
    PROCESS_AC_CLARIFICATION("processAcClarification"),
    PROCESS_AC_REJECTION("processAcRejection"),
    STANDSTILL_PERIOD("standstillPeriod"),
    START_AWARD_PERIOD("startAwardPeriod"),
    START_NEW_STAGE("startNewStage"),
    SUPPLIER_SIGNING_AC("supplierSigningAC"),
    SUSPEND_TENDER("suspendTender"),
    @Deprecated(message = "Using 'tenderPeriodEndEv'.", level = DeprecationLevel.WARNING)
    TENDER_PERIOD_END("tenderPeriodEnd"),
    TENDER_PERIOD_END_AUCTION("tenderPeriodEndAuction"),
    TENDER_PERIOD_END_EV("tenderPeriodEndEv"),
    TREASURY_APPROVING_AC("treasuryApprovingAC"),
    UNSUCCESSFUL_TENDER("tenderUnsuccessful"),
    UNSUSPEND_TENDER("unsuspendTender"),
    UPDATE_AC("updateAC"),
    UPDATE_AP("updateAP"),
    UPDATE_BID_DOCS("updateBidDocs"),
    UPDATE_CAN_DOCS("updateCanDocs"),
    UPDATE_CN("updateCN"),
    UPDATE_EI("updateEI"),
    UPDATE_FS("updateFS"),
    UPDATE_PN("updatePN"),
    UPDATE_TENDER_PERIOD("updateTenderPeriod"),
    VERIFICATION_AC("verificationAC");


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