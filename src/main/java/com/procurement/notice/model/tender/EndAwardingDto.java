package com.procurement.notice.model.tender;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.Lot;
import com.procurement.notice.model.ocds.Period;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "standstillPeriod",
        "lots"
})
public class EndAwardingDto {

    @JsonProperty("standstillPeriod")
    private final Period standstillPeriod;
    @JsonProperty("lots")
    private final List<Lot> lots;

    @JsonCreator
    public EndAwardingDto(@JsonProperty("standstillPeriod") final Period standstillPeriod,
                          @JsonProperty("lots") final List<Lot> lots) {
        this.standstillPeriod = standstillPeriod;
        this.lots = lots;
    }
}
