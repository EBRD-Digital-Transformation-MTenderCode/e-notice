package com.procurement.notice.controller;

import com.procurement.notice.model.dto.OcdsRelease;
import com.procurement.notice.service.MainServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/update")
public class MainController {

    private MainServiceImpl tenderService;

    public MainController(MainServiceImpl tenderService) {
        this.tenderService = tenderService;
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public ResponseEntity<String> insertTender(@RequestBody OcdsRelease data) {
        tenderService.insertData(data);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @RequestMapping(value = "/data", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateTender(@RequestBody OcdsRelease data) {
        tenderService.updateData(data);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
