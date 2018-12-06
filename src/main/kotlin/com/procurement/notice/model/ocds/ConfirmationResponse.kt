package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConfirmationResponse @JsonCreator constructor(

        val id: String?,

        val value: ConfirmationResponseValue?,

        val request: String?
)

data class ConfirmationResponseValue @JsonCreator constructor(

        val name: String?,

        val id: String?,

        val date: LocalDateTime?,

        val relatedPerson: RelatedPerson?,

        val verification: List<Verification>?
)

data class Verification @JsonCreator constructor(

        val type: String?,

        val value: String?,

        val rationale :String?
)
