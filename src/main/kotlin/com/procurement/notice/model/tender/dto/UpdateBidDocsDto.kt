package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Document

data class UpdateBidDocsDto @JsonCreator constructor(

    val bid: BidUpdateDocs
)

data class BidUpdateDocs @JsonCreator constructor(

    val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: HashSet<Document>
)
