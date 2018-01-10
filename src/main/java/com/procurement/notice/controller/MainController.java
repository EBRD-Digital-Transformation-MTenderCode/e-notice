package com.procurement.notice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.service.ReleaseService;
import com.procurement.notice.service.ReleaseServiceImpl;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/release")
public class MainController {

    private final ReleaseService releaseService;

    public MainController(final ReleaseServiceImpl releaseService) {
        this.releaseService = releaseService;
    }

    @RequestMapping(value = "/saveRecordRelease", method = RequestMethod.POST)
    public ResponseDto saveRecord(@RequestBody final RequestDto data) {
        return releaseService.saveRecordRelease(data);
    }

    @PostMapping(value = "/cn")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createCn(@RequestParam("cpid") final String cpid,
                                @RequestParam("stage") final String stage,
                                @RequestParam("operation") final String operation,
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                @RequestParam("startDate") final LocalDateTime releaseDate,
                                @Valid @RequestBody final JsonNode data) {
        return releaseService.createCn(cpid, stage, operation, releaseDate, data);
    }
}
