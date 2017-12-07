package com.procurement.notice.controller;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.service.PackageService;
import com.procurement.notice.service.PackageServiceImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/package")
public class PackageController {

    private final PackageService packageService;

    public PackageController(final PackageServiceImpl packageService) {
        this.packageService = packageService;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseDto savePackage(@RequestParam final String cpid, @RequestBody final RequestDto data) {
        return packageService.savePackage(cpid, data);
    }
}
