package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface BudgetService {

    ResponseDto createEin(String cpid,
                          String stage,
                          String operation,
                          JsonNode data);

    ResponseDto createFs(String cpid,
                         String stage,
                         String operation,
                         JsonNode data);
}
