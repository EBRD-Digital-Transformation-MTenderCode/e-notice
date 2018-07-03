package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Address @JsonCreator constructor(

        val streetAddress: String?,

        val locality: String?,

        val region: String?,

        val postalCode: String?,

        val countryName: String?
)
