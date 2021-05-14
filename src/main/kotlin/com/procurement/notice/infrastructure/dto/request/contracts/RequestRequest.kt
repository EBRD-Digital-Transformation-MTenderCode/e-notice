package com.procurement.notice.infrastructure.dto.request.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestRelatedOrganization

data class RequestRequest(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedPerson") @param:JsonProperty("relatedPerson") val relatedPerson: RequestRelatedOrganization?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedOrganization") @param:JsonProperty("relatedOrganization") val relatedOrganization: RequestRelatedOrganization?
)