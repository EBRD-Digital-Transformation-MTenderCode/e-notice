package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestClassification
import com.procurement.notice.infrastructure.dto.request.RequestPeriod
import com.procurement.notice.infrastructure.dto.request.RequestRecurrentProcurement
import com.procurement.notice.model.ocds.Value

data class RequestLot(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("internalId") @param:JsonProperty("internalId") val internalId: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: RequestClassification?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("status") @param:JsonProperty("status") val status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Deprecated(message = "Will not be used anymore")
    @field:JsonProperty("recurrentProcurement") @param:JsonProperty("recurrentProcurement") val recurrentProcurement: List<RequestRecurrentProcurement> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Deprecated(message = "Will not be used anymore")
    @field:JsonProperty("renewals") @param:JsonProperty("renewals") val renewals: List<RequestRenewal> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Deprecated(message = "Will not be used anymore")
    @field:JsonProperty("variants") @param:JsonProperty("variants") val variants: List<RequestVariant> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("contractPeriod") @param:JsonProperty("contractPeriod") val contractPeriod: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("placeOfPerformance") @param:JsonProperty("placeOfPerformance") val placeOfPerformance: RequestPlaceOfPerformance?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasOptions") @param:JsonProperty("hasOptions") val hasOptions: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("options") @param:JsonProperty("options") val options: List<RequestOption> = emptyList(),

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasRecurrence") @param:JsonProperty("hasRecurrence") val hasRecurrence: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("recurrence") @param:JsonProperty("recurrence") val recurrence: RequestRecurrence?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasRenewal") @param:JsonProperty("hasRenewal") val hasRenewal: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("renewal") @param:JsonProperty("renewal") val renewal: RequestRenewalV2?
)
