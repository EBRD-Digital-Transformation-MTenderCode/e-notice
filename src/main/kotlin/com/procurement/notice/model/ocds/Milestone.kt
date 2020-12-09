package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Milestone @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val type: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedItems: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var additionalInformation: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var dueDate: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedParties: List<RelatedParty>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var dateModified: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var dateMet: LocalDateTime?
)

data class RelatedParty @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?
)
