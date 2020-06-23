package com.procurement.notice.infrastructure.dto.request.qualification

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import com.procurement.notice.infrastructure.bind.scoring.ScoringDeserializer
import com.procurement.notice.infrastructure.bind.scoring.ScoringSerializer
import com.procurement.notice.infrastructure.dto.entity.qualification.QualificationStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class RequestQualification(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @JsonDeserialize(using = JsonDateTimeDeserializer::class)
    @JsonSerialize(using = JsonDateTimeSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("status") @param:JsonProperty("status") val status: QualificationStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedSubmission") @param:JsonProperty("relatedSubmission") val relatedSubmission: String?,

    @param:JsonDeserialize(using = ScoringDeserializer::class)
    @field:JsonSerialize(using = ScoringSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("scoring") @param:JsonProperty("scoring") val scoring: BigDecimal?
)