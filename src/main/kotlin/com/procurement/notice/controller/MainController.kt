package com.procurement.notice.controller

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.service.MainService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/release")
class MainController(private val mainService: MainService) {

    @PostMapping
    fun createRelease(@RequestParam cpId: String,
                      @RequestParam(required = false) ocId: String?,
                      @RequestParam stage: String,
                      @RequestParam(required = false) previousStage: String?,
                      @RequestParam operation: String,
                      @RequestParam(required = false) phase: String?,
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                      @RequestParam releaseDate: LocalDateTime,
                      @Valid @RequestBody data: JsonNode): ResponseEntity<ResponseDto<*>> {
        return ResponseEntity(
                mainService.createRelease(
                        cpId = cpId,
                        ocId = ocId,
                        newStage = stage,
                        previousStage = previousStage,
                        operation = operation,
                        phase = phase,
                        releaseDate = releaseDate,
                        data = data),
                HttpStatus.CREATED)
    }
}
