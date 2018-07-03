package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class PlaceOfPerformance @JsonCreator constructor(

        val address: Address?,

        val description: String?,

        val nutScode: String?
)