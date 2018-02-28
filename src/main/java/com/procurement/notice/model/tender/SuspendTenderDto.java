package com.procurement.notice.model.tender;

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
    @NonNull
    private final Tender tender;

    @JsonCreator
    public SuspendTenderDto(@JsonProperty("tender") final Tender tender) {
        this.tender = tender;
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
