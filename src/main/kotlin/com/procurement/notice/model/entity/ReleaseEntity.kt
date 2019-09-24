package com.procurement.notice.model.entity

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

data class ReleaseEntity(

    var cpId: String,

    var ocId: String,

    var publishDate: Date,

    var releaseDate: Date,

    var releaseId: String,

    var stage: String,

    var jsonData: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String? = ""
)
