package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class ContactPoint @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val email: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val telephone: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val faxNumber: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val url: String?
)