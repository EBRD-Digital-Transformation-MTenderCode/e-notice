package com.procurement.notice.service;

import com.procurement.notice.model.budget.ReleaseEI;
import com.procurement.notice.model.budget.ReleaseFS;
import com.procurement.notice.model.tender.dto.CheckFsDto;
import com.procurement.notice.model.tender.ms.Ms;
import org.springframework.stereotype.Service;

@Service
public interface OrganizationService {

    void processEiParties(ReleaseEI ei);

    void processFsParties(ReleaseFS fs);

    void processMsParties(Ms ms, CheckFsDto checkFs);

}
