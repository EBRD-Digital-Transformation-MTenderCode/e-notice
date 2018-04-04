package com.procurement.notice.service;

import com.procurement.notice.model.budget.EI;
import com.procurement.notice.model.budget.FS;
import com.procurement.notice.model.ocds.RelatedProcessType;
import com.procurement.notice.model.tender.dto.CheckFsDto;
import com.procurement.notice.model.tender.ms.Ms;
import com.procurement.notice.model.tender.record.Record;
import org.springframework.stereotype.Service;

@Service
public interface RelatedProcessService {

    void addFsRelatedProcessToEi(EI ei, String fsOcId);

    void addEiRelatedProcessToFs(FS fs, String eiOcId);

    void addMsRelatedProcessToEi(EI ei, String msOcId);

    void addMsRelatedProcessToFs(FS fs, String msOcId);

    void addEiFsRecordRelatedProcessToMs(Ms ms, CheckFsDto checkFs, String ocId, RelatedProcessType recordProcessType);

    void addMsRelatedProcessToRecord(Record record, String cpId);

    void addRecordRelatedProcessToMs(Record record, String msOcId, RelatedProcessType recordProcessType);

    void addPervRecordRelatedProcessToRecord(Record record, String prevRecordOcId, String msOcId);

}
