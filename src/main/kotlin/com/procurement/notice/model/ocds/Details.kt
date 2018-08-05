package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        val typeOfBuyer: String?,

        val mainGeneralActivity: String?,

        val mainSectoralActivity: String?,

        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        val nutsCode: String?,

        val scale: String?
)
