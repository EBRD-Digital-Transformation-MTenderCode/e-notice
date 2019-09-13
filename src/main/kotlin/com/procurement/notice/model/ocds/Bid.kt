package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Bid @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val tenderers: List<OrganizationReference>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: HashSet<Document>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementResponses: HashSet<RequirementResponse>?
)
