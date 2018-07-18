package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Address @JsonCreator constructor(

        val streetAddress: String?,

        val postalCode: String?,

        val addressDetails: AddressDetails?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AddressDetails(

        val country: Country?,

        val region: Region?,

        val locality: Locality?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Country(

        val scheme: String?,

        val id: String?,

        val description: String?,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Region(

        val scheme: String?,

        val id: String?,

        val description: String?,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Locality(

        val scheme: String?,

        val id: String?,

        val description: String?,

        val uri: String?
)