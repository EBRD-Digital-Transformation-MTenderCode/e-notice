package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Award @JsonCreator constructor(

        val id: String?,

        val title: String?,

        var description: String?,

        var status: String?,

        var statusDetails: String?,

        var date: LocalDateTime?,

        val value: Value?,

        val suppliers: List<OrganizationReference>?,

        var items: List<Item>?,

        val contractPeriod: Period?,

        var documents: List<Document>?,

        val amendments: List<Amendment>?,

        val amendment: Amendment?,

        val requirementResponses: List<RequirementResponse>?,

        val reviewProceedings: ReviewProceedings?,

        val relatedLots: List<String>?,

        val relatedBid: String?

)
