package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class Award @JsonCreator constructor(

        val id: String?,

        val title: String?,

        var description: String?,

        var status: AwardStatus?,

        var statusDetails: AwardStatus?,

        var date: LocalDateTime?,

        val value: Value?,

        val suppliers: HashSet<OrganizationReference>?,

        val items: HashSet<Item>?,

        val contractPeriod: Period?,

        var documents: HashSet<Document>?,

        val amendments: List<Amendment>?,

        val amendment: Amendment?,

        val relatedLots: List<String>?,

        val requirementResponses: HashSet<RequirementResponse>?,

        val reviewProceedings: ReviewProceedings?,

        val relatedBid: String?
)
