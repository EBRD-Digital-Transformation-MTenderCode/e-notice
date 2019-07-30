package com.procurement.notice.application.service.award

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class EvaluateAwardData(val award: Award) {
    data class Award(
        val id: String,
        val date: LocalDateTime,
        val description: String?,
        val status: String,
        val statusDetails: String,
        val relatedLots: List<UUID>,
        val value: Value,
        val suppliers: List<Supplier>,
        val documents: List<Document>?
    ) {

        data class Value(val amount: BigDecimal, val currency: String)

        data class Supplier(val id: String, val name: String)

        data class Document(
            val documentType: String,
            val id: String,
            val datePublished: LocalDateTime,
            val url: String,
            val title: String?,
            val description: String?,
            val relatedLots: List<UUID>
        )
    }
}