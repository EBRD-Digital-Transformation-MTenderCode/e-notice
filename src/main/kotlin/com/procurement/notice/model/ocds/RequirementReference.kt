package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class RequirementReference @JsonCreator constructor(

        val id: String?,

        val title: String?
)