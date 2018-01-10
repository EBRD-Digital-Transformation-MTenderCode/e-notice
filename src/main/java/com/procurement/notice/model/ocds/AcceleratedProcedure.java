package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isAcceleratedProcedure",
        "acceleratedProcedureJustification"
})
public class AcceleratedProcedure {
    @JsonProperty("isAcceleratedProcedure")
    @JsonPropertyDescription("A True/False field to indicate whether an accelerated procedure has been used for this " +
            "procurement")
    private final Boolean isAcceleratedProcedure;

    @JsonProperty("acceleratedProcedureJustification")
    @JsonPropertyDescription("Justification for using an accelerated procedure")
    private final String acceleratedProcedureJustification;

    @JsonCreator
    public AcceleratedProcedure(@JsonProperty("isAcceleratedProcedure") final Boolean isAcceleratedProcedure,
                                @JsonProperty("acceleratedProcedureJustification") final String
                                        acceleratedProcedureJustification) {
        this.isAcceleratedProcedure = isAcceleratedProcedure;
        this.acceleratedProcedureJustification = acceleratedProcedureJustification;
    }
}
