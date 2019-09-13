package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime


data class Award @JsonCreator constructor(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val id: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val title: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var description: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var status: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var statusDetails: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var date: LocalDateTime?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val value: Value?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val suppliers: List<OrganizationReference>?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var items: List<Item>?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val contractPeriod: Period?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var documents: List<Document>?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val amendments: List<Amendment>?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val amendment: Amendment?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val requirementResponses: List<RequirementResponse>?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val reviewProceedings: ReviewProceedings?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val relatedLots: List<String>?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val relatedBid: String?

)
