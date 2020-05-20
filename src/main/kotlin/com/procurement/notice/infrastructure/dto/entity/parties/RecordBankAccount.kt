package com.procurement.notice.infrastructure.dto.entity.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordAccountIdentifier
import com.procurement.notice.infrastructure.dto.entity.address.RecordAddress

data class RecordBankAccount(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("bankName") @param:JsonProperty("bankName") val bankName: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("address") @param:JsonProperty("address") val address: RecordAddress?,

    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: RecordAccountIdentifier,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: RecordAccountIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("additionalAccountIdentifiers") @param:JsonProperty("additionalAccountIdentifiers") val additionalAccountIdentifiers: List<RecordAccountIdentifier> = emptyList()
)