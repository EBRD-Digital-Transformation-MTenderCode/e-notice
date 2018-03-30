package com.procurement.notice.model.tender.ms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rationale",
        "budget"
})
public class MsPlanning {

    @JsonProperty("rationale")
    private final String rationale;

    @JsonProperty("budget")
    private final MsBudget budget;

    @JsonCreator
    public MsPlanning(@JsonProperty("budget") final MsBudget budget,
                      @JsonProperty("rationale") final String rationale) {
        this.budget = budget;
        this.rationale = rationale;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(budget)
                .append(rationale)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MsPlanning)) {
            return false;
        }
        final MsPlanning rhs = (MsPlanning) other;
        return new EqualsBuilder().append(rationale, rhs.rationale)
                .append(budget, rhs.budget)
                .isEquals();
    }
}
