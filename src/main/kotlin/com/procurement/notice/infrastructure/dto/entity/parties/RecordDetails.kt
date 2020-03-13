package com.procurement.notice.infrastructure.dto.entity.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordLegalForm

data class RecordDetails(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("typeOfBuyer") @param:JsonProperty("typeOfBuyer") val typeOfBuyer: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("typeOfSupplier") @param:JsonProperty("typeOfSupplier") val typeOfSupplier: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("mainEconomicActivities") @param:JsonProperty("mainEconomicActivities") val mainEconomicActivities: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("mainGeneralActivity") @param:JsonProperty("mainGeneralActivity") val mainGeneralActivity: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("mainSectoralActivity") @param:JsonProperty("mainSectoralActivity") val mainSectoralActivity: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("permits") @param:JsonProperty("permits") val permits: List<RecordPermits> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("bankAccounts") @param:JsonProperty("bankAccounts") val bankAccounts: List<RecordBankAccount> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("legalForm") @param:JsonProperty("legalForm") val legalForm: RecordLegalForm?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isACentralPurchasingBody") @param:JsonProperty("isACentralPurchasingBody") val isACentralPurchasingBody: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("nutsCode") @param:JsonProperty("nutsCode") val nutsCode: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("scale") @param:JsonProperty("scale") val scale: String?
)

