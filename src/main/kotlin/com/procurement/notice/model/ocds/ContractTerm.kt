package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ContractTerm @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val agreedMetrics: LinkedList<AgreedMetric>?
)

data class AgreedMetric @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val observations: LinkedList<Observation>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedRequirementId") @param:JsonProperty("relatedRequirementId") var relatedRequirementId: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: Unit? = null
)

data class Observation @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val notes: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val measure: Any?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val unit: ObservationUnit?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedRequirementId") @param:JsonProperty("relatedRequirementId") val relatedRequirementId: String?
)

data class ObservationUnit @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?
)
