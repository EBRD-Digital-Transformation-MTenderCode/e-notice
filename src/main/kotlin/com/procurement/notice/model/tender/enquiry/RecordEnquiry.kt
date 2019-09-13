package com.procurement.notice.model.tender.enquiry

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.OrganizationReference
import java.time.LocalDateTime

data class RecordEnquiry @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var author: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var answer: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var dateAnswered: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?
)
