package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Objectives @JsonCreator constructor(

        val types: List<ObjectivesType>?,

        val additionalInformation: String?
)