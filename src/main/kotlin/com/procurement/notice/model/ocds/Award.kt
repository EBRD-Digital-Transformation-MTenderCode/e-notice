package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Award @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val suppliers: List<OrganizationReference>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var items: List<Item>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val contractPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: List<Document>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val amendments: List<Amendment>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amendment: Amendment?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementResponses: List<RequirementResponse>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val reviewProceedings: ReviewProceedings?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedBid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val weightedValue: Value? = null
)
