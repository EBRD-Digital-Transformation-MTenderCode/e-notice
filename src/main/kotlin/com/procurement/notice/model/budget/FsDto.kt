package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.notice.databinding.MoneyDeserializer
import java.math.BigDecimal

data class FsDto @JsonCreator constructor(

        @field:JsonDeserialize(using = MoneyDeserializer::class)
        val totalAmount: BigDecimal,

        val fs: FS
)
