package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException
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
    X_BUDGET("x_budget"),
    X_CONTRACT("x_contract");

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
    PIN("Date of tender launch is determined")
}

enum class TenderTitle(val text: String) {

    PS("Preselection"),
    PQ("Prequalification"),
    EV("Evaluation"),
    PN("Planning Notice"),
    PIN("Prior Notice")
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
    AWARDED("awarded"),
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

enum class Stage {
    EI,
    FS,
    PS,
    PQ,
    PN,
    PIN,
    EV,
    CT
}

enum class Operation(val value: String) {
    CREATE_EI("createEI"),
    UPDATE_EI("updateEI"),
    CREATE_FS("createFS"),
    UPDATE_FS("updateFS"),
    CREATE_CN("createCN"),
    CREATE_PN("createPN"),
    CREATE_PIN("createPIN"),
    UPDATE_CN("updateCN"),
    UPDATE_TENDER_PERIOD("updateTenderPeriod"),
    CREATE_ENQUIRY("createEnquiry"),
    ADD_ANSWER("addAnswer"),
    UNSUSPEND_TENDER("unsuspendTender"),
    UNSUCCESSFUL_TENDER("tenderUnsuccessful"),
    TENDER_PERIOD_END("tenderPeriodEnd"),
    TENDER_PERIOD_END_EV("tenderPeriodEndEv"),
    SUSPEND_TENDER("suspendTender"),
    AWARD_BY_BID("awardByBid"),
    AWARD_BY_BID_EV("awardByBidEv"),
    AWARD_PERIOD_END("awardPeriodEnd"),
    AWARD_PERIOD_END_EV("awardPeriodEndEv"),
    STANDSTILL_PERIOD("standstillPeriod"),
    STANDSTILL_PERIOD_EV("standstillPeriodEv"),
    START_NEW_STAGE("startNewStage"),
    CREATE_PIN_ON_PN("createPINonPN"),
    CREATE_CN_ON_PN("createCNonPN"),
    CREATE_CN_ON_PIN("createCNonPIN");

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