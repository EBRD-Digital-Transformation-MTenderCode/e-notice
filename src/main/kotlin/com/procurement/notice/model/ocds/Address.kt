package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class Address @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val streetAddress: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val postalCode: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val addressDetails: AddressDetails?
)

data class AddressDetails(

    @field:Valid @field:NotNull
    val country: CountryDetails,

    @field:Valid @field:NotNull
    val region: RegionDetails,

    @field:Valid @field:NotNull
    val locality: LocalityDetails
)

data class CountryDetails(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var scheme: String?,

    @field:NotNull
    val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var uri: String?
)

data class RegionDetails(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var scheme: String?,

    @field:NotNull
    val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var uri: String?
)

data class LocalityDetails(

    @field:NotNull
    var scheme: String,

    @field:NotNull
    val id: String,

    @field:NotNull
    var description: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var uri: String?
)