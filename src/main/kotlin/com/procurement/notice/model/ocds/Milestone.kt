package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Milestone @JsonCreator constructor(

        var id: String?,

        var title: String?,

        var description: String?,

        val type: String?,

        var status: String?,

        var relatedItems: Set<String>?,

        var additionalInformation: String?,

        var dueDate: LocalDateTime?,

        var relatedParties: List<RelatedParty>?,

        var dateModified: LocalDateTime?,

        var dateMet: LocalDateTime?


)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RelatedParty @JsonCreator constructor(

        val id: String?,

        val name: String?
)