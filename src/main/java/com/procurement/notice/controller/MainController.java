package com.procurement.notice.controller;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.service.MainService;
import com.procurement.notice.service.MainServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/update")
public class MainController {

    private final MainService mainService;

    public MainController(final MainServiceImpl mainService) {
        this.mainService = mainService;
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public ResponseEntity<String> insertPackage(@RequestParam final String cpId, @RequestBody final RequestDto data) {
        mainService.savePackage(cpId, data);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
