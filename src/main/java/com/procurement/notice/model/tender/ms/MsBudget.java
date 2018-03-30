package com.procurement.notice.model.tender.ms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.BudgetBreakdown;
import com.procurement.notice.model.ocds.EuropeanUnionFunding;
import com.procurement.notice.model.ocds.Value;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "description",
        "amount",
        "isEuropeanUnionFunded",
        "europeanUnionFunding",
        "budgetBreakdown"
})
public class MsBudget {

    @JsonProperty("id")
    private String id;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("amount")
    private final Value amount;

    @JsonProperty("isEuropeanUnionFunded")
    private final Boolean isEuropeanUnionFunded;

    @JsonProperty("europeanUnionFunding")
    private final EuropeanUnionFunding europeanUnionFunding;

    @JsonProperty("budgetBreakdown")
    private final List<BudgetBreakdown> budgetBreakdown;

    @JsonCreator
    public MsBudget(@JsonProperty("id") final String id,
                    @JsonProperty("description") final String description,
                    @JsonProperty("amount") final Value amount,
                    @JsonProperty("isEuropeanUnionFunded") final Boolean isEuropeanUnionFunded,
                    @JsonProperty("europeanUnionFunding") final EuropeanUnionFunding europeanUnionFunding,
                    @JsonProperty("budgetBreakdown") final List<BudgetBreakdown> budgetBreakdown) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.isEuropeanUnionFunded = isEuropeanUnionFunded;
        this.europeanUnionFunding = europeanUnionFunding;
        this.budgetBreakdown = budgetBreakdown;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(description)
                .append(amount)
                .append(europeanUnionFunding)
                .append(isEuropeanUnionFunded)
                .append(budgetBreakdown)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MsBudget)) {
            return false;
        }
        final MsBudget rhs = (MsBudget) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(description, rhs.description)
                .append(amount, rhs.amount)
                .append(europeanUnionFunding, rhs.europeanUnionFunding)
                .append(isEuropeanUnionFunded, rhs.isEuropeanUnionFunded)
                .append(budgetBreakdown, rhs.budgetBreakdown)
                .isEquals();
    }
}
