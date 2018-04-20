package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

enum class Operation(val value: String) {
    CREATE_EI("createEI"),
    UPDATE_EI("updateEI"),
    CREATE_FS("createFS"),
    UPDATE_FS("updateFS"),
    CREATE_CN("createCN"),
    CREATE_PN("createPN"),
    CREATE_PIN("createPIN"),
    UPDATE_CN("updateCN"),
    CREATE_ENQUIRY("createEnquiry"),
    ADD_ANSWER("addAnswer"),
    UNSUSPEND_TENDER("unsuspendTender"),
    TENDER_PERIOD_END("tenderPeriodEnd"),
    SUSPEND_TENDER("suspendTender"),
    AWARD_BY_BID("awardByBid"),
    AWARD_PERIOD_END("awardPeriodEnd"),
    STANDSTILL_PERIOD("standstillPeriod"),
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