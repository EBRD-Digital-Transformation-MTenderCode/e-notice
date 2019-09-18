package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
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
    val observations: LinkedList<Observation>?
)

data class Observation @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val notes: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val measure: Any?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val unit: ObservationUnit?
)

data class ObservationUnit @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?
)
