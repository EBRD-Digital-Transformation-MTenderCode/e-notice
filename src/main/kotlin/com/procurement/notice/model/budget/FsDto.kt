package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.math.BigDecimal
import javax.validation.Valid

@JsonPropertyOrder("totalAmount", "fs")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsDto(

        @Valid
        @JsonProperty("totalAmount")
        val totalAmount: BigDecimal,

        @Valid
        @JsonProperty("fs")
        val fs: FS
)
