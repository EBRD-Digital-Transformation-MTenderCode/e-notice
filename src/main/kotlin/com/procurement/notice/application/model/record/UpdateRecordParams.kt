package com.procurement.notice.application.model.record

import com.procurement.notice.domain.extention.tryParse
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataValidationErrors
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.Result.Companion.failure
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.utils.tryMapping
import java.time.LocalDateTime

class UpdateRecordParams private constructor(
    val startDate: LocalDateTime,
    val data: RequestRelease
) {
    companion object {
        fun tryCreate(
            startDate: String,
            data: String
        ): Result<UpdateRecordParams, Fail> {

            val startDateParsed = startDate.tryParse()
                .doOnError { expectedFormat ->
                    return failure(
                        DataValidationErrors.DataFormatMismatch(
                            name = "startDate",
                            actualValue = startDate,
                            expectedFormat = expectedFormat
                        )
                    )
                }
                .get

            val dataParsed = tryMapping(data, RequestRelease::class.java)
                .doOnError { error ->
                    return failure(
                        Fail.Error.BadRequest(
                            description = "Can not parse 'data'.",
                            exception = error.exception
                        )
                    )
                }
                .get

            return Result.success(
                UpdateRecordParams(
                    startDate = startDateParsed,
                    data = dataParsed
                )
            )
        }
    }
}
