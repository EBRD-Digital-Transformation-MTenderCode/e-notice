package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Milestone @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val type: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedItems: Set<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var additionalInformation: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var dueDate: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedParties: List<RelatedParty>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var dateModified: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var dateMet: LocalDateTime?
)

data class RelatedParty @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?
)