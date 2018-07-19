package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Enquiry @JsonCreator constructor(

        val id: String?,

        val date: LocalDateTime?,

        val author: OrganizationReference?,

        val title: String?,

        val description: String,

        private val answer: String?,

        val dateAnswered: LocalDateTime?,

        val relatedItem: String?,

        val relatedLot: String?,

        val threadID: String?
)
