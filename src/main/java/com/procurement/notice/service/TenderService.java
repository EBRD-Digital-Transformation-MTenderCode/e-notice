package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.tender.pspq.PsPqRelease;
import org.springframework.stereotype.Service;

@Service
public interface TenderService {

    TenderEntity getTenderEntity(String cpId,
                                 String stage,
                                 PsPqRelease tender);

    ResponseDto createCn(String cpid,
                         String stage,
                         JsonNode data);

    ResponseDto tenderPeriodEnd(String cpid,
                                String stage,
                                JsonNode data);

    ResponseDto suspendTender(String cpid,
                              String stage,
                              JsonNode data);

    ResponseDto awardByBid(String cpid,
                           String stage,
                           JsonNode data);

    ResponseDto awardPeriodEnd(String cpid,
                               String stage,
                               JsonNode data);

    ResponseDto standstillPeriodEnd(String cpid,
                                    String stage,
                                    JsonNode data);

}

