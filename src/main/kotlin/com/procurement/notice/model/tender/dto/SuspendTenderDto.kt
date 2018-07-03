package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class SuspendTenderDto @JsonCreator constructor(

        val tender: TenderDto
)