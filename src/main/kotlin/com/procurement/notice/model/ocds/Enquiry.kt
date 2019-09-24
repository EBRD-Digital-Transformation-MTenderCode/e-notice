package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Enquiry @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val author: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    private val answer: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dateAnswered: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val threadID: String?
)
