package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

data class ContractTerm @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val agreedMetrics: LinkedList<AgreedMetric>?
)

data class AgreedMetric @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val observations: LinkedList<Observation>?
)

data class Observation @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val notes: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val measure: Any?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val unit: ObservationUnit?
)

data class ObservationUnit @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?
)