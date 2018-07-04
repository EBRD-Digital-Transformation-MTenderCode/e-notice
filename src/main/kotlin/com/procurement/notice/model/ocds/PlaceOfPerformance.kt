package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PlaceOfPerformance @JsonCreator constructor(

        val address: Address?,

        val description: String?,

        val nutScode: String?
)