package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class LegalProceedings @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val uri: String?
)
