package com.procurement.notice.infrastructure.dto

data class ApiVersion2(val major: Int, val minor: Int, val patch: Int) {
    companion object {
        fun valueOf(version: String): ApiVersion2 {
            val elements = version.split(".")
            if (elements.isEmpty() || elements.size > 3)
                throw IllegalArgumentException("Invalid value of the api version ($version).")

            val major: Int = elements[0].toIntOrNull()
                ?: throw IllegalArgumentException("Invalid value of the api version ($version).")

            val minor: Int = if (elements.size >= 2) {
                elements[1].toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid value of the api version ($version).")
            } else
                0

            val patch: Int = if (elements.size == 3) {
                elements[2].toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid value of the api version ($version).")
            } else
                0

            return ApiVersion2(major = major, minor = minor, patch = patch)
        }
    }

    override fun toString(): String = "$major.$minor.$patch"
}
