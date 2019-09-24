package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Transaction @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val source: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val payer: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val payee: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val providerOrganization: Identifier?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val receiverOrganization: Identifier?
)
