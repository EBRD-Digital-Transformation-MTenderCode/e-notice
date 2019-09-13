package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Lot @JsonCreator constructor(

    val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val options: List<Option>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val recurrentProcurement: List<RecurrentProcurement>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val renewals: List<Renewal>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val variants: List<Variant>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val contractPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val placeOfPerformance: PlaceOfPerformance?
)
