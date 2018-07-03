package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

data class RequirementGroup @JsonCreator constructor(

        val id: String?,

        val description: String?,

        val requirements: HashSet<Requirement>?
)