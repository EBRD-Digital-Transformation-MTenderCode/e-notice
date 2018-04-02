package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.Period;
import com.procurement.notice.model.tender.enquiry.RecordEnquiry;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "enquiry",
        "tender",
        "tenderPeriod",
        "enquiryPeriod"
})
public class UnsuspendTenderDto {

    @JsonProperty("enquiry")
    private final RecordEnquiry enquiry;

    @JsonProperty("tender")
    private final TenderDto tender;

    @JsonProperty("tenderPeriod")
    private final Period tenderPeriod;

    @JsonProperty("enquiryPeriod")
    private final Period enquiryPeriod;

    @JsonCreator
    public UnsuspendTenderDto(@JsonProperty("enquiry") final RecordEnquiry enquiry,
                              @JsonProperty("tender") final TenderDto tender,
                              @JsonProperty("tenderPeriod") final Period tenderPeriod,
                              @JsonProperty("enquiryPeriod") final Period enquiryPeriod) {
        this.enquiry = enquiry;
        this.tender = tender;
        this.tenderPeriod = tenderPeriod;
        this.enquiryPeriod = enquiryPeriod;
    }
}
