package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.TenderStatus;
import com.procurement.notice.model.ocds.TenderStatusDetails;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonPropertyOrder({
        "tender"
})
public class SuspendTenderDto {

    @JsonProperty("tender")
    private final TenderDto tender;

    @JsonCreator
    public SuspendTenderDto(@JsonProperty("tender") final TenderDto tender) {
        this.tender = tender;
    }


}
