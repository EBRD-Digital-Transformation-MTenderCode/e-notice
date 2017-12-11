package com.procurement.notice.controller;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.service.RecordService;
import com.procurement.notice.service.RecordServiceImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/package")
public class MainController {

    private final RecordService recordService;

    public MainController(final RecordServiceImpl recordService) {
        this.recordService = recordService;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseDto saveRecord(@RequestParam final String cpid, @RequestBody final RequestDto data) {
        return recordService.savePackage(cpid, data);
    }
}
