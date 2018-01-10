package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "hasOptions",
        "optionDetails"
})
public class Option {
    @JsonProperty("hasOptions")
    @JsonPropertyDescription("A True/False field to indicate if lot options will be accepted. Required by the EU")
    private final Boolean hasOptions;

    @JsonProperty("optionDetails")
    @JsonPropertyDescription("Further information about the lot options that will be accepted. Required by the EU")
    private final String optionDetails;

    @JsonCreator
    public Option(@JsonProperty("hasOptions") final Boolean hasOptions,
                  @JsonProperty("optionDetails") final String optionDetails) {
        this.hasOptions = hasOptions;
        this.optionDetails = optionDetails;
    }
}
