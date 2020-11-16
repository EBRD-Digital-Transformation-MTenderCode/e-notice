package com.procurement.notice.infrastructure.dto.entity.address

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordAddressDetails(

    @field:JsonProperty("country") @param:JsonProperty("country") val country: RecordCountryDetails,

    @field:JsonProperty("region") @param:JsonProperty("region") val region: RecordRegionDetails,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: RecordLocalityDetails?
)