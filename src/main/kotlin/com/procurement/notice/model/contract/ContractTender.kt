package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Classification
import com.procurement.notice.model.ocds.PlaceOfPerformance

data class ContractTender @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var lots: HashSet<ContractTenderLot>? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var classification: Classification? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val mainProcurementCategory: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethod: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodDetails: String? = null
)

data class ContractTenderLot @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val placeOfPerformance: PlaceOfPerformance? = null
)