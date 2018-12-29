package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Lot


data class ConfirmCanDto @JsonCreator constructor(

        val lots: HashSet<Lot>,

        val cans: HashSet<Can>
)