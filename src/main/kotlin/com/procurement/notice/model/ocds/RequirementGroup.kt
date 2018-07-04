package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RequirementGroup @JsonCreator constructor(

        val id: String?,

        val description: String?,

        val requirements: HashSet<Requirement>?
)