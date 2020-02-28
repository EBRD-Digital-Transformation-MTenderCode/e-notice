package com.procurement.notice.controller

import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.model.bpe.errorResponse2
import com.procurement.notice.model.bpe.getBy
import com.procurement.notice.service.CommandService2
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toNode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/command2")
class CommandController2(private val commandService: CommandService2) {
    companion object {
        private val log = LoggerFactory.getLogger(CommandController2::class.java)
    }

    @PostMapping
    fun command(@RequestBody requestBody: String): ResponseEntity<ApiResponse2> {
        if (log.isDebugEnabled)
            log.debug("RECEIVED COMMAND: '$requestBody'.")

        val node = try {
            requestBody.toNode()
        } catch (expected: Exception) {
            log.debug("Error.", expected)
            val response =
                errorResponse2(
                    exception = expected,
                    version = GlobalProperties.App.apiVersion
                )
            return ResponseEntity(response, HttpStatus.OK)
        }

        val id = try {
            UUID.fromString(node.getBy("id").asText())
        } catch (expected: Exception) {
            log.debug("Error.", expected)
            val response = errorResponse2(
                exception = expected,
                version = GlobalProperties.App.apiVersion
            )
            return ResponseEntity(response, HttpStatus.OK)
        }
        val version = try {
            ApiVersion2.valueOf(node.getBy("version").asText())
        } catch (expected: Exception) {
            log.debug("Error.", expected)
            val response = errorResponse2(
                id = id,
                exception = expected,
                version = GlobalProperties.App.apiVersion
            )
            return ResponseEntity(response, HttpStatus.OK)
        }

        val response = try {
            commandService.execute(node)
                .also { response ->
                    if (log.isDebugEnabled)
                        log.debug("RESPONSE (id: '${id}'): '${toJson(response)}'.")
                }
        } catch (expected: Exception) {
            log.debug("Error.", expected)
            errorResponse2(
                exception = expected,
                id = id,
                version = version
            )
        }
        return ResponseEntity(response, HttpStatus.OK)
    }
}
