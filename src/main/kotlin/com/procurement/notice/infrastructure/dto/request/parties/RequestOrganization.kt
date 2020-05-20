package com.procurement.notice.infrastructure.dto.request.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestContactPoint
import com.procurement.notice.infrastructure.dto.request.RequestIdentifier
import com.procurement.notice.infrastructure.dto.request.address.RequestAddress
import com.procurement.notice.model.ocds.PartyRole

data class RequestOrganization(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("name") @param:JsonProperty("name") val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: RequestIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("address") @param:JsonProperty("address") val address: RequestAddress?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<RequestIdentifier> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: RequestContactPoint?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: RequestDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<RequestPerson> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("buyerProfile") @param:JsonProperty("buyerProfile") val buyerProfile: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("roles") @param:JsonProperty("roles") val roles: List<PartyRole> = emptyList()
)
