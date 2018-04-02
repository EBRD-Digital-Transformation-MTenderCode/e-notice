package com.procurement.notice.service;

import com.procurement.notice.model.budget.ReleaseEI;
import com.procurement.notice.model.budget.ReleaseFS;
import com.procurement.notice.model.ocds.RelatedProcessType;
import com.procurement.notice.model.tender.dto.CheckFsDto;
import com.procurement.notice.model.tender.ms.Ms;
import com.procurement.notice.model.tender.record.Record;
import org.springframework.stereotype.Service;

@Service
public interface RelatedProcessService {

    void addFsRelatedProcessToEi(ReleaseEI ei, String fsOcId);

    void addEiRelatedProcessToFs(ReleaseFS fs, String eiOcId);

    void addMsRelatedProcessToEi(ReleaseEI ei, String msOcId);

    void addMsRelatedProcessToFs(ReleaseFS fs, String msOcId);

    void addRelatedProcessToMs(Ms ms, CheckFsDto checkFs, String ocId, RelatedProcessType recordProcessType);

    void addMsRelatedProcess(Record release, String cpId);

    void addRelatedProcessToPq(Record release, Ms ms);

}
