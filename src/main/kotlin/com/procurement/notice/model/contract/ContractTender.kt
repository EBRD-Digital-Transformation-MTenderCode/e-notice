package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Classification
import com.procurement.notice.model.ocds.PlaceOfPerformance

data class ContractTender @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var lots: HashSet<ContractTenderLot>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var classification: Classification? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val mainProcurementCategory: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethod: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodDetails: String? = null
)

data class ContractTenderLot @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val placeOfPerformance: PlaceOfPerformance? = null
)
