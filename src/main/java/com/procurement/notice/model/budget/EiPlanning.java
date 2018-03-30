package com.procurement.notice.model.budget;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "budget",
        "rationale"
})
public class EiPlanning {

    @JsonProperty("budget")
    private final EiBudget budget;


    @JsonProperty("rationale")
    private final String rationale;

    @JsonCreator
    public EiPlanning(@JsonProperty("budget") final EiBudget budget,
                      @JsonProperty("rationale") final String rationale) {
        this.budget = budget;
        this.rationale = rationale;
    }
}
