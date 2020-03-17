package com.procurement.notice.application.model

import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.asSuccess

fun parseCpid(value: String): Result<Cpid, DataErrors.Validation.DataMismatchToPattern> =
    Cpid.tryCreate(cpid = value)
        .doReturn { expectedPattern ->
            return Result.failure(
                DataErrors.Validation.DataMismatchToPattern(
                    name = "cpid",
                    pattern = expectedPattern,
                    actualValue = value
                )
            )
        }
        .asSuccess()

fun parseOcid(value: String): Result<Ocid, DataErrors.Validation.DataMismatchToPattern> =
    Ocid.tryCreate(ocid = value)
        .doReturn { expectedPattern ->
            return Result.failure(
                DataErrors.Validation.DataMismatchToPattern(
                    name = "ocid",
                    pattern = expectedPattern,
                    actualValue = value
                )
            )
        }
        .asSuccess()

fun extractStage(ocid: Ocid): Result<Stage, DataErrors.Parsing> =
    Stage.tryOfOcid(ocid = ocid)
        .doReturn { expectedPattern ->
            return Result.failure(
                DataErrors.Parsing(
                    description = "Cannot parse 'stage' from 'ocid'. Ocid='${ocid.value}'. Expected pattern: ${expectedPattern}"
                )
            )
        }
        .asSuccess()
