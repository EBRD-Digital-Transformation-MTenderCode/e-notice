package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Identifier @JsonCreator constructor(

        val scheme: String?,

        val id: String?,

        val legalName: String?,

        val uri: String?
)
