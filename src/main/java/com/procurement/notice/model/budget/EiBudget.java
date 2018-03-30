package com.procurement.notice.model.budget;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.Period;
import com.procurement.notice.model.ocds.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "period",
        "amount"
})
public class EiBudget {

    @JsonProperty("id")
    private String id;

    @JsonProperty("period")
    private final Period period;

    @JsonProperty("amount")
    private Value amount;

    @JsonCreator
    public EiBudget(@JsonProperty("id") final String id,
                    @JsonProperty("period") final Period period,
                    @JsonProperty("amount") final Value amount
    ) {
        this.id = id;
        this.period = period;
        this.amount = amount;
    }
}
