package com.procurement.notice.application.model

import com.procurement.notice.domain.fail.error.DataValidationErrors
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.Result.Companion.success

fun parseCpid(value: String): Result<Cpid, DataValidationErrors.DataMismatchToPattern> {
    val cpid = Cpid.tryCreateOrNull(value = value)
    return if (cpid != null)
        success(cpid)
    else
        Result.failure(
            DataValidationErrors.DataMismatchToPattern(
                name = "cpid",
                pattern = Cpid.pattern,
                actualValue = value
            )
        )
}

fun parseOcid(value: String): Result<Ocid, DataValidationErrors.DataMismatchToPattern> {
    val ocid = Ocid.tryCreateOrNull(value = value)
    return if (ocid != null)
        success(ocid)
    else
        Result.failure(
            DataValidationErrors.DataMismatchToPattern(
                name = "ocid",
                pattern = Ocid.pattern,
                actualValue = value
            )
        )
}

