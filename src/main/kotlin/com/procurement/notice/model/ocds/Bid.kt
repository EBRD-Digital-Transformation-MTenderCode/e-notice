package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Bid @JsonCreator constructor(

        val id: String?,

        var date: LocalDateTime?,

        var status: String?,

        var statusDetails: String?,

        val tenderers: List<OrganizationReference>?,

        val value: Value?,

        var documents: HashSet<Document>?,

        val relatedLots: List<String>?,

        val requirementResponses: HashSet<RequirementResponse>?
)


