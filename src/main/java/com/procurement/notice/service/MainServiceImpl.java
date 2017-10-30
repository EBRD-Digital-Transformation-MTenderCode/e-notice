package com.procurement.notice.service;

import com.procurement.notice.config.properties.OCDSProperties;
import com.procurement.notice.model.dto.OcdsRelease;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    private TenderService tenderService;

    private OCDSProperties ocdsProperties;

    public MainServiceImpl(TenderService tenderService,
                           OCDSProperties ocdsProperties) {
        this.tenderService = tenderService;
        this.ocdsProperties = ocdsProperties;
    }

    @Override
    public void insertData(OcdsRelease data) {
        Date addedDate = new Date();
        String osId = data.getOcid();
        if (Objects.isNull(osId)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sDate = sdf.format(addedDate);
            osId = ocdsProperties.getPrefix() + "-" + sDate + "-" + addedDate.getTime();
        }
        tenderService.insertData(osId, addedDate, data.getTender());
    }

    @Override
    public void updateData(OcdsRelease data) {
        Date addedDate = new Date();
        String osId = data.getOcid();
        if (Objects.nonNull(osId)) { //update
            tenderService.updateData(osId, addedDate, data.getTender());
        }
    }
}

