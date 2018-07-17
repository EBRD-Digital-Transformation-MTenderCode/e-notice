package com.procurement.notice.model.entity

import java.util.*

data class ReleaseEntity(

        var cpId: String,

        var ocId: String,

        var releaseDate: Date,

        var releaseId: String,

        var stage: String,

        var jsonData: String,

        var status: String? = ""
)


