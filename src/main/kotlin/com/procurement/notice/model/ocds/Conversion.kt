package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude

data class Conversion(
    val id: String,
    val relatesTo: String,
    val relatedItem: String,
    val rationale: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    val coefficients: List<Coefficient>
)