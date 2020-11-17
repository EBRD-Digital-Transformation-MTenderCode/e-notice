package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.domain.utils.EnumElementProvider

enum class TenderStatusDetails(@JsonValue override val key: String): EnumElementProvider.Key {
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

    override fun toString(): String = key

    companion object : EnumElementProvider<TenderStatusDetails>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = TenderStatusDetails.orThrow(name)
    }
}
