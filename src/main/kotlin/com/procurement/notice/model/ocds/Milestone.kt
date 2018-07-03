package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime
import java.util.*

data class Milestone @JsonCreator constructor(

        private val id: String?,

        val title: String?,

        val type: MilestoneType?,

        val description: String?,

        val code: String?,

        val dueDate: LocalDateTime?,

        val dateMet: LocalDateTime?,

        val dateModified: LocalDateTime?,

        val status: MilestoneStatus?,

        val documents: HashSet<Document>?,

        val relatedLots: List<String>?,

        val relatedParties: List<OrganizationReference>?,

        val additionalInformation: String?
)