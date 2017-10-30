package com.procurement.notice.service;

import com.procurement.notice.model.dto.OcdsRelease;
import org.springframework.stereotype.Service;

@Service
public interface MainService {

    void insertData(OcdsRelease data);

    void updateData(OcdsRelease data);
}
