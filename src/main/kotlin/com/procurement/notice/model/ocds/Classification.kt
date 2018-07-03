package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Classification @JsonCreator constructor(

        val scheme: ClassificationScheme?,

        val id: String?,

        val description: String?,

        val uri: String?
)
