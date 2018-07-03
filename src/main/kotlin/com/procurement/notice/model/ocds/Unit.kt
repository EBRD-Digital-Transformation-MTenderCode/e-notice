package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Unit @JsonCreator constructor(

        val name: String?,

        val value: Value?,

        val scheme: UnitScheme?,

        val id: String?,

        val uri: String?
)
