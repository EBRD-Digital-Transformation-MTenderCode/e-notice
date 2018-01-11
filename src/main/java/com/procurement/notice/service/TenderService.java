package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface TenderService {

    ResponseDto createCn(String cpid,
                         String stage,
                         String operation,
                         LocalDateTime releaseDate,
                         JsonNode data);
}
