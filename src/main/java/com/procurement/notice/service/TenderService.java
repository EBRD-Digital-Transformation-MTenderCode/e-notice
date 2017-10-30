package com.procurement.notice.service;

import com.procurement.notice.model.dto.Release;
import org.springframework.stereotype.Service;

@Service
public interface TenderService {

    void insertData(Release data);

}
