package com.procurement.notice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
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

//@Validated
//@RestController
//@RequestMapping("/release")
public class TenderController {
//
//    private final TenderService tenderService;
//
//    private final EnquiryService enquiryService;
//
//    public TenderController(final TenderServiceImpl tenderService,
//                            final EnquiryService enquiryService) {
//        this.tenderService = tenderService;
//        this.enquiryService = enquiryService;
//    }
//
//    @PostMapping(value = "/tender")
//    public ResponseEntity<ResponseDto> createCn(@RequestParam final String cpId,
//                                                @RequestParam final String stage,
//                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//                                                @RequestParam final LocalDateTime releaseDate,
//                                                @Valid @RequestBody final JsonNode data) {
//        return new ResponseEntity<>(
//                tenderService.createCn(cpId, stage, releaseDate, data),
//                HttpStatus.CREATED);
//    }
//
//    @PostMapping(value = "/enquiry")
//    public ResponseEntity<ResponseDto> createEnquiry(@RequestParam final String cpId,
//                                                     @RequestParam final String ocId,
//                                                     @RequestParam final String stage,
//                                                     @Valid @RequestBody final JsonNode data) {
//        return new ResponseEntity<>(
//                enquiryService.createEnquiry(cpId, ocId, stage, data),
//                HttpStatus.CREATED);
//    }
//
}
