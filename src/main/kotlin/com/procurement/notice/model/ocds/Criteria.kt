package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.enums.CriteriaSource

data class Criteria(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("source") @param:JsonProperty("source") val source: CriteriaSource?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonProperty("requirementGroups") @param:JsonProperty("requirementGroups") val requirementGroups: List<RequirementGroup>,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatesTo") @param:JsonProperty("relatesTo") val relatesTo: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification?
)