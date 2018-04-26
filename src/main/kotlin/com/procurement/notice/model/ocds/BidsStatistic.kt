package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "measure", "date", "value", "notes", "relatedLot")
data class BidsStatistic(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("measure")
        val measure: Measure?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val date: LocalDateTime?,

        @JsonProperty("value")
        val value: Double?,

        @JsonProperty("notes")
        val notes: String?,

        @JsonProperty("relatedLot")
        val relatedLot: String?
)
