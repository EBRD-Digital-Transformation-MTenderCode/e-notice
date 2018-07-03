package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Details @JsonCreator constructor(

        val typeOfBuyer: TypeOfBuyer?,

        val mainGeneralActivity: MainGeneralActivity?,

        val mainSectoralActivity: MainSectoralActivity?,

        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        val nutsCode: String?,

        val scale: Scale?
)
