package com.procurement.notice.application.model.record.create

import com.procurement.notice.domain.extention.tryParseLocalDateTime
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataValidationErrors
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.Result.Companion.failure
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.utils.tryDeserialize
import java.time.LocalDateTime

class CreateRecordParams private constructor(
    val date: LocalDateTime,
    val data: RequestRelease
) {
    companion object {
        fun tryCreate(
            date: String,
            data: String
        ): Result<CreateRecordParams, Fail> {

            val startDateParsed = date.tryParseLocalDateTime()
                .doReturn { expectedFormat ->
                    return failure(
                        DataValidationErrors.DataFormatMismatch(
                            name = "date",
                            actualValue = date,
                            expectedFormat = expectedFormat
                        )
                    )
                }

            val dataParsed = tryDeserialize(data, RequestRelease::class.java)
                .doReturn { error ->
                    return failure(
                        Fail.Error.BadRequest(
                            description = "Can not parse 'data'.",
                            json = data,
                            exception = error.exception
                        )
                    )
                }

            return Result.success(
                CreateRecordParams(date = startDateParsed, data = dataParsed)
            )
        }
    }
}
