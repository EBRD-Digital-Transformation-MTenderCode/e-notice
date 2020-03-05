package com.procurement.notice.domain.fail.error

import com.procurement.notice.domain.fail.Fail

sealed class BadRequestErrors(
    numberError: String,
    override val description: String
) : Fail.Error("BR-") {

    override val code: String = prefix + numberError

    class EntityNotFound(entityName: String, by: String) : BadRequestErrors(
        numberError = "01",
        description = "Entity '$entityName' not found $by"
    )

    class ParseToObject(objectName: String) : BadRequestErrors(
        numberError = "02",
        description = "Error binding json to an object '$objectName'"
    )
}
