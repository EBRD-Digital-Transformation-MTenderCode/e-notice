package com.procurement.notice.service;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface MainService {

    ResponseDto savePackage(String cpId, RequestDto data);
}
