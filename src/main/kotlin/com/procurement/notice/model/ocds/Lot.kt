package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Lot @JsonCreator constructor(

    val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val options: List<Option>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val recurrentProcurement: List<RecurrentProcurement>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val renewals: List<Renewal>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val variants: List<Variant>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val contractPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val placeOfPerformance: PlaceOfPerformance?
)
