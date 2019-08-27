package com.procurement.notice.controller

import com.procurement.notice.exception.EnumException
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.model.bpe.CommandMessage
import com.procurement.notice.model.bpe.CommandType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.bpe.getEnumExceptionResponseDto
import com.procurement.notice.model.bpe.getErrorExceptionResponseDto
import com.procurement.notice.model.bpe.getExceptionResponseDto
import com.procurement.notice.service.CommandService
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val commandService: CommandService) {
    companion object {
        private val log = LoggerFactory.getLogger(CommandController::class.java)
    }

    @PostMapping
    fun command(@RequestBody requestBody: String): ResponseEntity<ResponseDto> {
        if (log.isDebugEnabled)
            log.debug("RECEIVED COMMAND: '$requestBody'.")
        val cm: CommandMessage = toObject(CommandMessage::class.java, requestBody)

        val response = execute(cm)

        if (log.isDebugEnabled)
            log.debug("RESPONSE (operation-id: '${cm.context.operationId}'): '${toJson(response)}'.")
        return ResponseEntity(response, HttpStatus.OK)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.CREATE_RELEASE -> commandService.execute(cm)
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception): ResponseDto {
        return when (ex) {
            is ErrorException -> getErrorExceptionResponseDto(ex)
            is EnumException -> getEnumExceptionResponseDto(ex)
            else -> getExceptionResponseDto(ex)
        }
    }
}



