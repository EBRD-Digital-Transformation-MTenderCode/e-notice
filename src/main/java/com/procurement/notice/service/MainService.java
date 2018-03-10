package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.model.bpe.ResponseDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface MainService {

    ResponseDto createRelease(String cpId,
                              String ocId,
                              String stage,
                              String previousStage,
                              String operation,
                              String phase,
                              LocalDateTime releaseDate,
                              JsonNode data);
}

