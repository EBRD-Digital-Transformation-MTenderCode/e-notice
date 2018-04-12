package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.point.databinding.JsonDateDeserializer
import java.time.LocalDateTime

@JsonPropertyOrder("id", "measure", "date", "value", "notes", "relatedLot")
data class BidsStatistic(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("measure")
        val measure: Measure?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        val date: LocalDateTime?,

        @JsonProperty("value")
        val value: Double?,

        @JsonProperty("notes")
        val notes: String?,

        @JsonProperty("relatedLot")
        val relatedLot: String?
)
