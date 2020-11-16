package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.criteria.requirement.RequirementDeserializer
import com.procurement.notice.infrastructure.bind.criteria.requirement.RequirementSerializer
import com.procurement.notice.model.ocds.Requirement

data class RequestRequirementGroup(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonDeserialize(using = RequirementDeserializer::class)
    @JsonSerialize(using = RequirementSerializer::class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("requirements") @param:JsonProperty("requirements") val requirements: List<Requirement> = emptyList()
)

