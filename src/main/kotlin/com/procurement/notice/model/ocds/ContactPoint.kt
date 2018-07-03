package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class ContactPoint @JsonCreator constructor(

        val name: String?,

        val email: String?,

        val telephone: String?,

        val faxNumber: String?,

        val url: String?
)