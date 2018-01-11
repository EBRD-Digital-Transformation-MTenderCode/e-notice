package com.procurement.notice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.service.BudgetService;
import com.procurement.notice.service.TenderService;
import com.procurement.notice.service.TenderServiceImpl;
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

    private final TenderService releaseService;

    private final BudgetService budgetService;

    public MainController(final TenderServiceImpl releaseService,
                          final BudgetService budgetService) {
        this.releaseService = releaseService;
        this.budgetService = budgetService;
    }

    @PostMapping(value = "/cn")
    public ResponseEntity<ResponseDto> createCn(@RequestParam("cpid") final String cpid,
                                                @RequestParam("stage") final String stage,
                                                @RequestParam("operation") final String operation,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                @RequestParam("startDate") final LocalDateTime releaseDate,
                                                @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                releaseService.createCn(cpid, stage, operation, releaseDate, data),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/ein")
    public ResponseEntity<ResponseDto> createEin(@RequestParam("cpid") final String cpid,
                                                 @RequestParam("stage") final String stage,
                                                 @RequestParam("operation") final String operation,
                                                 @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.createEin(cpid, stage, operation, data),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/fs")
    public ResponseEntity<ResponseDto> createFs(@RequestParam("cpid") final String cpid,
                                                 @RequestParam("stage") final String stage,
                                                 @RequestParam("operation") final String operation,
                                                 @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.createFs(cpid, stage, operation, data),
                HttpStatus.CREATED);
    }

}
