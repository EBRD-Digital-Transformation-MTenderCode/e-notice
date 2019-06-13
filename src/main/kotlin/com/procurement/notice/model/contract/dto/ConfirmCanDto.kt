package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Lot

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ConfirmCanDto @JsonCreator constructor(

        val lots: HashSet<Lot>,

        val cans: HashSet<Can>
)