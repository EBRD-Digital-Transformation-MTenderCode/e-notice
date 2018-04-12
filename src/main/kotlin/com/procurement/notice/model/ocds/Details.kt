package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("typeOfBuyer", "mainGeneralActivity", "mainSectoralActivity", "isACentralPurchasingBody", "NUTSCode", "scale")
data class Details(

        @JsonProperty("typeOfBuyer")
        val typeOfBuyer: TypeOfBuyer?,

        @JsonProperty("mainGeneralActivity")
        val mainGeneralActivity: MainGeneralActivity?,

        @JsonProperty("mainSectoralActivity")
        val mainSectoralActivity: MainSectoralActivity?,

        @JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        @JsonProperty("NUTSCode")
        val nutsCode: String?,

        @JsonProperty("scale")
        val scale: Scale?
)
