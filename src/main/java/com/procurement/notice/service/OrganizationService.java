package com.procurement.notice.service;

import com.procurement.notice.model.budget.EI;
import com.procurement.notice.model.budget.FS;
import com.procurement.notice.model.ocds.Bids;
import com.procurement.notice.model.tender.dto.CheckFsDto;
import com.procurement.notice.model.tender.ms.Ms;
import com.procurement.notice.model.tender.record.Record;
import org.springframework.stereotype.Service;

@Service
public interface OrganizationService {

    void processEiParties(EI ei);

    void processFsParties(FS fs);

    void processMsParties(Ms ms, CheckFsDto checkFs);

    void processPartiesFromBids(Record record, Bids bids);

}
