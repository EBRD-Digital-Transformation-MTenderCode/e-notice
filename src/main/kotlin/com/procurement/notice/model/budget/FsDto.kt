package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class FsDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val ei: EiForFs?,

    val fs: FS
)
