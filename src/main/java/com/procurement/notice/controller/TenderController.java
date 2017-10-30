package com.procurement.notice.controller;

import com.procurement.notice.model.dto.Release;
import com.procurement.notice.service.TenderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/update")
public class TenderController {

    private TenderServiceImpl tenderService;

    public TenderController(TenderServiceImpl tenderService) {
        this.tenderService = tenderService;
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public ResponseEntity<String> insertTender(@RequestBody Release data) {
        tenderService.insertData(data);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

}
