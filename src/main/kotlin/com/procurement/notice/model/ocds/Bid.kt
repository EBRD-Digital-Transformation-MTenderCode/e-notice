package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Bid @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val tenderers: List<OrganizationReference>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("items") @param:JsonProperty("items") val items: List<BidItem> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: List<Document>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementResponses: List<RequirementResponse>?
)
