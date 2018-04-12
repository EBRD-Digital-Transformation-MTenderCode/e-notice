package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("streetAddress", "locality", "region", "postalCode", "countryName")
data class Address(

        @JsonProperty("streetAddress")
        val streetAddress: String?,

        @JsonProperty("locality")
        val locality: String?,

        @JsonProperty("region")
        val region: String?,

        @JsonProperty("postalCode")
        val postalCode: String?,

        @JsonProperty("countryName")
        val countryName: String?
)
