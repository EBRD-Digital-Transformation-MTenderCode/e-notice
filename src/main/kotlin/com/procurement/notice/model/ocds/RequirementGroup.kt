package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*


data class RequirementGroup @JsonCreator constructor(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val id: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val description: String?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val requirements: HashSet<Requirement>?
)