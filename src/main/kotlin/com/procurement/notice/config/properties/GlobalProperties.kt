package com.procurement.notice.config.properties

import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.infrastructure.io.orThrow
import java.util.*

object GlobalProperties {

    val service = Service()

    object App {
        val apiVersion = ApiVersion2(major = 1, minor = 0, patch = 0)
    }

    class Service(
        val id: String = "2",
        val name: String = "e-notice",
        val version: String = loadVersion()
    )

    private fun loadVersion(): String {
        val gitProps: Properties = try {
            GlobalProperties::class.java.getResourceAsStream("/git.properties")
                .use { stream ->
                    Properties().apply { load(stream) }
                }
        } catch (expected: Exception) {
            throw IllegalStateException(expected)
        }
        return gitProps.orThrow("git.commit.id.abbrev")
    }

}
