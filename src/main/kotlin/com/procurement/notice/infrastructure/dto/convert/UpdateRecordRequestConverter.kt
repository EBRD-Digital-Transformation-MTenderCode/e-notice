package com.procurement.notice.infrastructure.dto.convert


import com.procurement.notice.application.model.record.UpdateRecordParams
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.infrastructure.handler.record.update.UpdateRecordRequest

fun UpdateRecordRequest.convert(): Result<UpdateRecordParams, Fail> {
    return UpdateRecordParams.tryCreate(
        date = this.date,
        data = this.data
    )
}
