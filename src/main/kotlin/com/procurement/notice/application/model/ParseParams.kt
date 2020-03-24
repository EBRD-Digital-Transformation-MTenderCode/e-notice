package com.procurement.notice.application.model

import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.Result.Companion.success
import com.procurement.notice.domain.utils.asSuccess

fun parseCpid(value: String): Result<Cpid, DataErrors.Validation.DataMismatchToPattern> {
    val cpid = Cpid.tryCreateOrNull(value = value)
    return if (cpid != null)
        success(cpid)
    else
        Result.failure(
            DataErrors.Validation.DataMismatchToPattern(
                name = "cpid",
                pattern = Cpid.pattern,
                actualValue = value
            )
        )
}

fun parseOcid(value: String): Result<Ocid, DataErrors.Validation.DataMismatchToPattern> {
    val ocid = Ocid.tryCreateOrNull(value = value)
    return if (ocid != null)
        success(ocid)
    else
        Result.failure(
            DataErrors.Validation.DataMismatchToPattern(
                name = "ocid",
                pattern = Ocid.pattern,
                actualValue = value
            )
        )
}

fun extractStage(ocid: Ocid): Result<Stage, DataErrors.Parsing> =
    tryOfOcid(ocid = ocid)
        .doReturn { expectedPattern ->
            return Result.failure(
                DataErrors.Parsing(
                    description = "Cannot parse 'stage' from 'ocid'. Ocid='${ocid.toString()}'. Expected pattern: ${expectedPattern}"
                )
            )
        }
        .asSuccess()
