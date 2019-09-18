package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class ContactPoint @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val email: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val telephone: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val faxNumber: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val url: String?
)
