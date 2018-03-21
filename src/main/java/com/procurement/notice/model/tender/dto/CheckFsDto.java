package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.OrganizationReference;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ei",
        "buyer",
        "funder",
        "payer"
})
public class CheckFsDto {

    @JsonProperty("ei")
    private final List<String> ei;
    @JsonProperty("buyer")
    private final List<OrganizationReference> buyer;
    @JsonProperty("funder")
    private final List<OrganizationReference> funder;
    @JsonProperty("payer")
    private final List<OrganizationReference> payer;

    @JsonCreator
    public CheckFsDto(@JsonProperty("ei") final List<String> ei,
                      @JsonProperty("buyer") final List<OrganizationReference> buyer,
                      @JsonProperty("funder") final List<OrganizationReference> funder,
                      @JsonProperty("payer") final List<OrganizationReference> payer) {
        this.ei = ei;
        this.buyer = buyer;
        this.funder = funder;
        this.payer = payer;
    }
}
