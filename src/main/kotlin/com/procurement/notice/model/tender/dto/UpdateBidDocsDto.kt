package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Document

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateBidDocsDto @JsonCreator constructor(

        val bid: BidUpdateDocs
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class BidUpdateDocs @JsonCreator constructor(

        val id: String,

        var documents: HashSet<Document>

)