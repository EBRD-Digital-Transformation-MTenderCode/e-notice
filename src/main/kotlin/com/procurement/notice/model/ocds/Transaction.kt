package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class Transaction @JsonCreator constructor(

        val id: String?,

        val source: String?,

        val date: LocalDateTime?,

        val value: Value?,

        val payer: OrganizationReference?,

        val payee: OrganizationReference?,

        val uri: String?,

        val amount: Value?,

        val providerOrganization: Identifier?,

        val receiverOrganization: Identifier?
)
