package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Classification
import com.procurement.notice.model.ocds.PlaceOfPerformance

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ContractTender @JsonCreator constructor(

        val id: String? = null,

        var title: String? = null,

        var description: String? = null,

        var lots: HashSet<ContractTenderLot>? = null,

        var classification: Classification? = null,

        val mainProcurementCategory: String? = null,

        val procurementMethod: String? = null,

        val procurementMethodDetails: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContractTenderLot @JsonCreator constructor(

        val id: String? = null,

        var title: String? = null,

        var description: String? = null,

        val placeOfPerformance: PlaceOfPerformance? = null
)