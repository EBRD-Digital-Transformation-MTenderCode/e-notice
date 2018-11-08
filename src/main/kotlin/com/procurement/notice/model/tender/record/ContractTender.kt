package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

@JsonInclude(JsonInclude.Include.NON_NULL)
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