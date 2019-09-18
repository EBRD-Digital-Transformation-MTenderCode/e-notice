package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class ConfirmationResponse @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: ConfirmationResponseValue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val request: String?
)

data class ConfirmationResponseValue @JsonCreator constructor(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedPerson: RelatedPerson?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val verification: List<Verification>?
)

data class Verification @JsonCreator constructor(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val type: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?
)
