package com.procurement.notice.config.properties

import com.procurement.notice.infrastructure.dto.ApiVersion2
import java.util.*

object GlobalProperties {

    val service = Service()

    object App {
        val apiVersion = ApiVersion2(major = 1, minor = 0, patch = 0)

    }

    class Service(
        val id: String = "2",
        val name: String = "e-notice",
        val version: String = getGitProperties()
    )

    private fun getGitProperties(): String {
        val prop = Properties()
        val loader = Thread.currentThread().contextClassLoader
        val stream = loader.getResourceAsStream("git.properties")
        if (stream != null) {
            prop.load(stream)
            return prop.getProperty("git.commit.id.abbrev")
        } else {
            throw RuntimeException("Unable to find git.commit.id.abbrev")
        }
    }
}
