package com.procurement.notice.controller

import com.procurement.notice.exception.EnumException
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.bpe.getEnumExceptionResponseDto
import com.procurement.notice.model.bpe.getErrorExceptionResponseDto
import com.procurement.notice.model.bpe.getExceptionResponseDto
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(OK)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception): ResponseDto {
        return when (ex) {
            is ErrorException -> getErrorExceptionResponseDto(ex)
            is EnumException -> getEnumExceptionResponseDto(ex)
            else -> getExceptionResponseDto(ex)
        }
    }

}
