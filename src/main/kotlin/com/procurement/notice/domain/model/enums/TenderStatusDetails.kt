package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class TenderStatusDetails(@JsonValue val value: String) {
    AUCTION("auction"),
    AWARDED_CONTRACT_PREPARATION("awardedContractPreparation"),
    AWARDED_STANDSTILL("awardedStandStill"),
    AWARDED_SUSPENDED("awardedSuspended"),
    AWARDING("awarding"),
    CANCELLATION("cancellation"),
    CLARIFICATION("clarification"),
    COMPLETE("complete"),
    EMPTY("empty"),
    LACK_OF_SUBMISSIONS("lackOfSubmissions"),
    NEGOTIATION("negotiation"),
    PLANNED("planned"),
    PLANNING("planning"),
    QUALIFICATION("qualification"),
    QUALIFICATION_STAND_STILL("qualificationStandStill"),
    SUSPENDED("suspended"),
    TENDERING("tendering");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, TenderStatusDetails> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): TenderStatusDetails = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = TenderStatusDetails::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
