package com.procurement.notice.controller

import com.procurement.notice.exception.EnumException
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.model.bpe.ResponseDetailsDto
import com.procurement.notice.model.bpe.ResponseDto
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
            is ErrorException -> ResponseDto(false, getErrors(ex.code, ex.msg), null)
            is EnumException -> ResponseDto(false, getErrors(ex.code, ex.msg), null)
            else -> ResponseDto(false, getErrors("Exception", ex.message), null)
        }
    }


    private fun getErrors(code: String, error: String?) =
            listOf(ResponseDetailsDto(code = "400.02.$code", message = error!!))

}
