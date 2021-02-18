package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Lot @JsonCreator constructor(

    val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val internalId: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Deprecated(message = "Will not be used anymore")
    val recurrentProcurement: List<RecurrentProcurement>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Deprecated(message = "Will not be used anymore")
    val renewals: List<Renewal>? = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Deprecated(message = "Will not be used anymore")
    val variants: List<Variant>? = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val contractPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val placeOfPerformance: PlaceOfPerformance?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasOptions") @param:JsonProperty("hasOptions") val hasOptions: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("options") @param:JsonProperty("options") val options: List<Option>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasRecurrence") @param:JsonProperty("hasRecurrence") val hasRecurrence: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("recurrence") @param:JsonProperty("recurrence") val recurrence: Recurrence?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasRenewal") @param:JsonProperty("hasRenewal") val hasRenewal: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("renewal") @param:JsonProperty("renewal") val renewal: RenewalV2?
)
