package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Objectives @JsonCreator constructor(

        val types: List<ObjectivesType>?,

        val additionalInformation: String?
)