package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Milestone @JsonCreator constructor(

        private val id: String?,

        val title: String?,

        val type: String?,

        val description: String?,

        val code: String?,

        val dueDate: LocalDateTime?,

        val dateMet: LocalDateTime?,

        val dateModified: LocalDateTime?,

        val status: String?,

        val documents: HashSet<Document>?,

        val relatedLots: List<String>?,

        val relatedParties: List<OrganizationReference>?,

        val additionalInformation: String?
)