package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Transaction @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val source: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val payer: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val payee: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val providerOrganization: Identifier?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val receiverOrganization: Identifier?
)
