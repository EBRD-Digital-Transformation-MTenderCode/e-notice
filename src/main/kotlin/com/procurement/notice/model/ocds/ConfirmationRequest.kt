package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class ConfirmationRequest @JsonCreator constructor(

    var id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var type: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var relatesTo: String?,

    val relatedItem: String,

    val source: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var requestGroups: Set<RequestGroup>?
)

data class RequestGroup @JsonCreator constructor(

    val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requests: Set<Request>
)

data class Request @JsonCreator constructor(

    val id: String,

    val title: String,

    val description: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedPerson: RelatedPerson?
)

data class RelatedPerson @JsonCreator constructor(

    val id: String,

    val name: String
)
