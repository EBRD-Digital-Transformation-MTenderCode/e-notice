package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.model.record.create.CreateRecordParams
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.infrastructure.handler.record.create.CreateRecordRequest

fun CreateRecordRequest.convert(): Result<CreateRecordParams, Fail> {
    return CreateRecordParams.tryCreate(
        date = this.date,
        data = this.data
    )
}
