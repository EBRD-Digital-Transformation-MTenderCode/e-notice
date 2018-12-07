package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Milestone
import com.procurement.notice.model.ocds.Period

data class EndAwardPeriodDto @JsonCreator constructor(

        val contract: EndAwardPeriodContract,

        val tender: EndAwardPeriodTender,

        val lot: EndAwardPeriodLot,

        val awardPeriod: Period
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EndAwardPeriodContract @JsonCreator constructor(

        var status: String,

        var statusDetails: String,

        val milestones: List<Milestone>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EndAwardPeriodTender @JsonCreator constructor(

        var status: String,

        var statusDetails: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EndAwardPeriodLot @JsonCreator constructor(

        val id: String,

        var status: String,

        var statusDetails: String
)