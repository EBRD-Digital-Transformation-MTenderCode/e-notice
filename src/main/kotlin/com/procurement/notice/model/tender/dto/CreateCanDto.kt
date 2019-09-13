package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.contract.Can

data class CreateCanDto @JsonCreator constructor(

    val can: Can
)
