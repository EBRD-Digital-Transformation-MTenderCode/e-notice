package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Contract

data class Can @JsonCreator constructor(

        val contract: Contract
)
