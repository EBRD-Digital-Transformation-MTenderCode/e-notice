package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Bid @JsonCreator constructor(

        val id: String?,

        var date: LocalDateTime?,

        var status: BidStatus?,

        var statusDetails: BidStatusDetails?,

        val tenderers: List<OrganizationReference>?,

        val value: Value?,

        var documents: HashSet<Document>?,

        val relatedLots: List<String>?,

        val requirementResponses: HashSet<RequirementResponse>?
)


