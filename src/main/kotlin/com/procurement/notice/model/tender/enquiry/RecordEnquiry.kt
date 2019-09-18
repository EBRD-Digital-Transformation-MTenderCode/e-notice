package com.procurement.notice.model.tender.enquiry

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.OrganizationReference
import java.time.LocalDateTime

data class RecordEnquiry @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var author: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var answer: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var dateAnswered: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?
)
