package com.procurement.notice.infrastructure.dto.request.address

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestAddressDetails(

    @field:JsonProperty("country") @param:JsonProperty("country") val country: RequestCountryDetails,

    @field:JsonProperty("region") @param:JsonProperty("region") val region: RequestRegionDetails,

    @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: RequestLocalityDetails
)