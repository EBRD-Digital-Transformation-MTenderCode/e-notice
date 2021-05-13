package com.procurement.notice.infrastructure.dto.entity.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordRelatedOrganization

data class RecordRequest(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedPerson") @param:JsonProperty("relatedPerson") val relatedPerson: RecordRelatedOrganization?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedOrganization") @param:JsonProperty("relatedOrganization") val relatedOrganization: RecordRelatedOrganization?
)