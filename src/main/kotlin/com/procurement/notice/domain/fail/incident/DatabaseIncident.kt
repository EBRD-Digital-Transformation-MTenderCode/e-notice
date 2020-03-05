package com.procurement.notice.domain.fail.incident

import com.procurement.notice.domain.fail.Fail

sealed class DatabaseIncident(code: String, description: String, exception: Exception) :
    Fail.Incident(code, description,exception) {

    class Database(exception: Exception) : DatabaseIncident(
        code = "01",
        description = "Database incident",
        exception = exception
    )
}
