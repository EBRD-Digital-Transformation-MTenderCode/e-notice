package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LegalProceedings @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val uri: String?
)
