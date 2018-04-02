package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.model.tender.record.Record;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface ReleaseService {

    ReleaseEntity getReleaseEntity(String cpId,
                                   String stage,
                                   Record tender);

    ResponseDto createCn(String cpid,
                         String stage,
                         LocalDateTime releaseDate,
                         JsonNode data);

    ResponseDto tenderPeriodEnd(String cpid,
                                String stage,
                                LocalDateTime releaseDate,
                                JsonNode data);

    ResponseDto suspendTender(String cpid,
                              String stage,
                              LocalDateTime releaseDate,
                              JsonNode data);

    ResponseDto awardByBid(String cpid,
                           String stage,
                           LocalDateTime releaseDate,
                           JsonNode data);

    ResponseDto awardPeriodEnd(String cpid,
                               String stage,
                               LocalDateTime releaseDate,
                               JsonNode data);

    ResponseDto standstillPeriodEnd(String cpid,
                                    String stage,
                                    LocalDateTime releaseDate,
                                    JsonNode data);

    ResponseDto startNewStage(String cpid,
                              String stage,
                              String previousStage,
                              LocalDateTime releaseDate,
                              JsonNode data);


}

