package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class LegalProceedings @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
