package com.procurement.notice.controller

import com.procurement.notice.model.bpe.CommandMessage
import com.procurement.notice.model.bpe.CommandType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.service.MainService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val mainService: MainService) {

    @PostMapping
    fun command(@RequestBody commandMessage: CommandMessage): ResponseEntity<ResponseDto> {
        return ResponseEntity(execute(commandMessage), HttpStatus.OK)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.CREATE_RELEASE -> mainService.createRelease(cm)
        }
    }
}



