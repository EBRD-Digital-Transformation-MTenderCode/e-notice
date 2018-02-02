package com.procurement.notice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.service.BudgetService;
import com.procurement.notice.service.EnquiryService;
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

    private final EnquiryService enquiryService;

    public MainController(final TenderServiceImpl releaseService,
                          final BudgetService budgetService,
                          final EnquiryService enquiryService) {
        this.releaseService = releaseService;
        this.budgetService = budgetService;
        this.enquiryService = enquiryService;
    }

    @PostMapping(value = "/ein")
    public ResponseEntity<ResponseDto> createEin(final String cpId,
                                                 final String stage,
                                                 @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.createEin(cpId, stage, data),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/ein")
    public ResponseEntity<ResponseDto> updateEin(final String cpId,
                                                 final String stage,
                                                 @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.updateEin(cpId, stage, data),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/fs")
    public ResponseEntity<ResponseDto> createFs(final String cpId,
                                                final String stage,
                                                @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.createFs(cpId, stage, data),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/fs")
    public ResponseEntity<ResponseDto> updateFs(final String cpId,
                                                final String ocId,
                                                final String stage,
                                                @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.updateFs(cpId, ocId, stage, data),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/cn")
    public ResponseEntity<ResponseDto> createCn(final String cpId,
                                                final String stage,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime releaseDate,
                                                @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                releaseService.createCn(cpId, stage, releaseDate, data),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/enquiry")
    public ResponseEntity<ResponseDto> createCn(final String cpId,
                                                final String ocId,
                                                final String stage,
                                                @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                enquiryService.createEnquiry(cpId, ocId, stage, data),
                HttpStatus.CREATED);
    }

}
