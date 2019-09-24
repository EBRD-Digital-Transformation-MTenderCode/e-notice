package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class PlaceOfPerformance @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val address: Address?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val nutScode: String?
)
