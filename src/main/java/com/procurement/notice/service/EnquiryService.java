package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryService {

    ResponseDto createEnquiry(String cpid,
                         String ocid,
                         String stage,
                         JsonNode data);

    ResponseDto updateEnquiry(String cpid,
                              String ocid,
                              String stage,
                              JsonNode data);
}
