package com.procurement.notice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.service.MainService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/release")
public class MainController {

    private final MainService mainService;

    public MainController(final MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createRelease(@RequestParam final String cpId,
                                                     @RequestParam(required = false) final String ocId,
                                                     @RequestParam final String stage,
                                                     @RequestParam(required = false) final String previousStage,
                                                     @RequestParam final String operation,
                                                     @RequestParam(required = false) final String phase,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam(required = false) final LocalDateTime releaseDate,
                                                     @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                mainService.createRelease(cpId,
                        ocId,
                        stage,
                        previousStage,
                        operation,
                        phase,
                        releaseDate,
                        data),
                HttpStatus.CREATED);
    }
}
