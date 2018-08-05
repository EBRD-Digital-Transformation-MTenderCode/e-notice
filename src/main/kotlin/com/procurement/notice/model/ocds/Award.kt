package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Award @JsonCreator constructor(

        val id: String?,

        val title: String?,

        var description: String?,

        var status: String?,

        var statusDetails: String?,

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
