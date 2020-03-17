package com.procurement.notice.application.model

import com.procurement.notice.domain.utils.Result

class Stage private constructor(val value: String) {
    companion object {
        private val regex = "(?<=[A-Za-z0-9]{4}-[A-Za-z0-9]{6}-[A-Z]{2}-[0-9]{13}-)([A-Z]{2})(?=-[0-9]{13})".toRegex()

        fun tryOfOcid(ocid: Ocid): Result<Stage, String> {
            val stage = regex.find(ocid.value)?.value

            return if (stage != null)
                Result.success(Stage(value = stage))
            else
                Result.failure(regex.pattern)
        }
    }
}
