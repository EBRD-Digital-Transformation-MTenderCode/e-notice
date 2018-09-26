package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Lot @JsonCreator constructor(

        val id: String,

        val title: String?,

        val description: String?,

        var status: LotStatus?,

        var statusDetails: LotStatusDetails?,

        val value: Value?,

        val options: List<Option>?,

        val recurrentProcurement: List<RecurrentProcurement>?,

        val renewals: List<Renewal>?,

        val variants: List<Variant>?,

        val contractPeriod: Period?,

        val placeOfPerformance: PlaceOfPerformance?
)
