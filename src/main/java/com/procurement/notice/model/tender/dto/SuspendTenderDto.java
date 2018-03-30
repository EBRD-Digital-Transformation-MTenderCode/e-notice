package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SuspendTenderDto {

    @JsonProperty("tender")
    private final TenderDto tender;

    @JsonCreator
    public SuspendTenderDto(@JsonProperty("tender") final TenderDto tender) {
        this.tender = tender;
    }

}
