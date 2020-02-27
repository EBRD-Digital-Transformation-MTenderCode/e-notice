package com.procurement.notice.infrastructure.dto.request.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestAccountIdentifier
import com.procurement.notice.infrastructure.dto.request.address.RequestAddress

data class RequestBankAccount(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("bankName") @param:JsonProperty("bankName") val bankName: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("address") @param:JsonProperty("address") val address: RequestAddress?,

    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: RequestAccountIdentifier,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: RequestAccountIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("additionalAccountIdentifiers") @param:JsonProperty("additionalAccountIdentifiers") val additionalAccountIdentifiers: List<RequestAccountIdentifier> = emptyList()
)