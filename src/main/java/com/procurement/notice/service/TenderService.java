package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.tender.ReleaseTender;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface TenderService {

    TenderEntity getTenderEntity(String cpId,
                                 String stage,
                                 ReleaseTender tender);

    ResponseDto createCn(String cpid,
                         String stage,
                         JsonNode data);

    ResponseDto tenderPeriodEnd(String cpid,
                         String stage,
                         JsonNode data);

    ResponseDto suspendTender(String cpid,
                              String stage,
                              JsonNode data);
}
