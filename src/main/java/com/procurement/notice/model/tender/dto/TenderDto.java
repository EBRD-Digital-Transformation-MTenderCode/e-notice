package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.procurement.notice.model.ocds.TenderStatus;
import com.procurement.notice.model.ocds.TenderStatusDetails;
import lombok.Getter;

@Getter
public class TenderDto {

    @JsonProperty("status")
    private final TenderStatus status;

    @JsonProperty("statusDetails")
    private final TenderStatusDetails statusDetails;

    @JsonCreator
    TenderDto(@JsonProperty("status") final TenderStatus status,
              @JsonProperty("statusDetails") final TenderStatusDetails statusDetails) {
        this.status = status;
        this.statusDetails = statusDetails;
    }
}
