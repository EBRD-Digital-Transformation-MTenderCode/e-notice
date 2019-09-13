package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Enquiry @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val author: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private val answer: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val dateAnswered: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val threadID: String?
)
