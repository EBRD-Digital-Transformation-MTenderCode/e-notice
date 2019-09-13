package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class ConfirmationResponse @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: ConfirmationResponseValue?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val request: String?
)

data class ConfirmationResponseValue @JsonCreator constructor(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedPerson: RelatedPerson?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val verification: List<Verification>?
)

data class Verification @JsonCreator constructor(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val type: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?
)
