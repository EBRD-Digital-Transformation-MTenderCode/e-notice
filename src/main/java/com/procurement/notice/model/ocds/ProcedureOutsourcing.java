package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "procedureOutsourced",
        "outsourcedTo"
})
public class ProcedureOutsourcing {
    @JsonProperty("procedureOutsourced")
    @JsonPropertyDescription("A True/False field to indicate whether the procurement procedure has been outsourced")
    private final Boolean procedureOutsourced;

    @JsonProperty("outsourcedTo")
    @JsonPropertyDescription("An organization.")
    private final Organization outsourcedTo;

    @JsonCreator
    public ProcedureOutsourcing(@JsonProperty("procedureOutsourced") final Boolean procedureOutsourced,
                                @JsonProperty("outsourcedTo") final Organization outsourcedTo) {
        this.procedureOutsourced = procedureOutsourced;
        this.outsourcedTo = outsourcedTo;
    }
}
