package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface BudgetService {

    ResponseDto createEi(String cpid,
                         String stage,
                         JsonNode data);

    ResponseDto updateEi(String cpid,
                         String stage,
                         JsonNode data);

    ResponseDto createFs(String cpid,
                         String stage,
                         JsonNode data);

    ResponseDto updateFs(String cpid,
                         String ocid,
                         String stage,
                         JsonNode data);
}
