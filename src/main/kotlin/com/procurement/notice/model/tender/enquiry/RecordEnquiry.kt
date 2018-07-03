package com.procurement.notice.model.tender.enquiry

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.OrganizationReference
import java.time.LocalDateTime

data class RecordEnquiry @JsonCreator constructor(

        val id: String?,

        val date: LocalDateTime?,

        val author: OrganizationReference?,

        val title: String?,

        val description: String?,

        var answer: String?,

        var dateAnswered: LocalDateTime?,

        val relatedItem: String?,

        val relatedLot: String?
)
