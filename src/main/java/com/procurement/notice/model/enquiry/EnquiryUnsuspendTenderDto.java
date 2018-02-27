package com.procurement.notice.model.enquiry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.Enquiry;
import com.procurement.notice.model.ocds.Period;
import com.procurement.notice.model.ocds.TenderStatus;
import com.procurement.notice.model.ocds.TenderStatusDetails;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonPropertyOrder({
        "enquiry",
        "tender",
        "tenderPeriod",
        "enquiryPeriod"
})
public class EnquiryUnsuspendTenderDto {

    @JsonProperty("enquiry")
    @NonNull
    private final Enquiry enquiry;
    @JsonProperty("tender")
    @NonNull
    private final Tender tender;
    @JsonProperty("tenderPeriod")
    @NonNull
    private final Period tenderPeriod;
    @JsonProperty("enquiryPeriod")
    @NonNull
    private final Period enquiryPeriod;

    @JsonCreator
    public EnquiryUnsuspendTenderDto(@JsonProperty("enquiry") final Enquiry enquiry,
                                     @JsonProperty("tender") final Tender tender,
                                     @JsonProperty("tenderPeriod") final Period tenderPeriod,
                                     @JsonProperty("enquiryPeriod") final Period enquiryPeriod) {
        this.enquiry = enquiry;
        this.tender = tender;
        this.tenderPeriod = tenderPeriod;
        this.enquiryPeriod = enquiryPeriod;
    }

    @Getter
    public class Tender {
        @JsonProperty("status")
        @NonNull
        private final TenderStatus status;
        @JsonProperty("statusDetails")
        @NonNull
        private final TenderStatusDetails statusDetails;

        @JsonCreator
        Tender(@JsonProperty("status") final TenderStatus status,
               @JsonProperty("statusDetails") final TenderStatusDetails statusDetails) {
            this.status = status;
            this.statusDetails = statusDetails;
        }
    }

}
