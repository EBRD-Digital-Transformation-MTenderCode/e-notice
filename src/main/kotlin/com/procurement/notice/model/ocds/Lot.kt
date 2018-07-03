package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Lot @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        var status: TenderStatus?,

        var statusDetails: TenderStatusDetails?,

        val value: Value?,

        val options: List<Option>?,

        val recurrentProcurement: List<RecurrentProcurement>?,

        val renewals: List<Renewal>?,

        val variants: List<Variant>?,

        val contractPeriod: Period?,

        val placeOfPerformance: PlaceOfPerformance?
)
