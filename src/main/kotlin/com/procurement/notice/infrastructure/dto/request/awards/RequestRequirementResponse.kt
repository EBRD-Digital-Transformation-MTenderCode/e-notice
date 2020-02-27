package com.procurement.notice.infrastructure.dto.request.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.application.model.RequirementRsValue
import com.procurement.notice.infrastructure.bind.criteria.requirement.value.RequirementValueDeserializer
import com.procurement.notice.infrastructure.bind.criteria.requirement.value.RequirementValueSerializer
import com.procurement.notice.infrastructure.dto.request.RequestOrganizationReference
import com.procurement.notice.infrastructure.dto.request.RequestPeriod

data class RequestRequirementResponse(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonDeserialize(using = RequirementValueDeserializer::class)
    @JsonSerialize(using = RequirementValueSerializer::class)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: RequirementRsValue,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("requirement") @param:JsonProperty("requirement") val requirement: RequestRequirementReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedTenderer") @param:JsonProperty("relatedTenderer") val relatedTenderer: RequestOrganizationReference?
)
