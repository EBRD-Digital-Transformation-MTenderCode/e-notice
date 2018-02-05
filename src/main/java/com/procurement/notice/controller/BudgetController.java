package com.procurement.notice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.service.BudgetService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;


    public BudgetController(final BudgetService budgetService) {

        this.budgetService = budgetService;
    }

    @PostMapping(value = "/ein")
    public ResponseEntity<ResponseDto> createEin(@RequestParam final String cpId,
                                                 @RequestParam final String stage,
                                                 @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.createEin(cpId, stage, data),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/ein")
    public ResponseEntity<ResponseDto> updateEin(@RequestParam final String cpId,
                                                 @RequestParam final String stage,
                                                 @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.updateEin(cpId, stage, data),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/fs")
    public ResponseEntity<ResponseDto> createFs(@RequestParam final String cpId,
                                                @RequestParam final String stage,
                                                @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.createFs(cpId, stage, data),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/fs")
    public ResponseEntity<ResponseDto> updateFs(@RequestParam final String cpId,
                                                @RequestParam final String ocId,
                                                @RequestParam final String stage,
                                                @Valid @RequestBody final JsonNode data) {
        return new ResponseEntity<>(
                budgetService.updateFs(cpId, ocId, stage, data),
                HttpStatus.CREATED);
    }
}
