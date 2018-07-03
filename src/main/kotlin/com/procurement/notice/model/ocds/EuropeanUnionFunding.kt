package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class EuropeanUnionFunding @JsonCreator constructor(

        val projectIdentifier: String?,

        val projectName: String?,

        val uri: String?
)
