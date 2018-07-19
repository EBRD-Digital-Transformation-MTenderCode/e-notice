package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Unit @JsonCreator constructor(

        val name: String?,

        val value: Value?,

        val scheme: String?,

        val id: String?,

        val uri: String?
)
