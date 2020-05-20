package com.procurement.notice.infrastructure.dto.entity.address

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordAddress(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("addressDetails") @param:JsonProperty("addressDetails") val addressDetails: RecordAddressDetails?
)
