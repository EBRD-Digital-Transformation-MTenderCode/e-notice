package com.procurement.notice.model.tender.ms;

import com.fasterxml.jackson.annotation.*;
import com.procurement.notice.model.ocds.OrganizationReference;
import com.procurement.notice.model.ocds.Period;
import com.procurement.notice.model.ocds.Value;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "id",
        "description",
        "amount",
        "period",
        "sourceParty"
})
public class MsBudgetBreakdown {
    @JsonProperty("id")
    @NotNull
    private final String id;

    @JsonProperty("description")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String description;

    @JsonProperty("amount")
    @Valid
    @NotNull
    private final Value amount;

    @JsonProperty("period")
    @Valid
    @NotNull
    private final Period period;

    @JsonProperty("sourceParty")
    @Valid
    @NotNull
    private final MsOrganizationReference sourceParty;

    @JsonCreator
    public MsBudgetBreakdown(@JsonProperty("id") final String id,
                             @JsonProperty("description") final String description,
                             @JsonProperty("amount") final Value amount,
                             @JsonProperty("period") final Period period,
                             @JsonProperty("sourceParty") final MsOrganizationReference sourceParty) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.period = period;
        this.sourceParty = sourceParty;
    }
}
