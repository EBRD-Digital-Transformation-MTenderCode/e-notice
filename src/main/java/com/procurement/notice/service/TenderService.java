package com.procurement.notice.service;

import com.procurement.notice.model.dto.Tender;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public interface TenderService {

    void insertData(String ocId, Date addedDate, Tender data);

    void updateData(String ocId, Date addedDate, Tender data);

}
