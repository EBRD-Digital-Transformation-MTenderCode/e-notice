package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "buyerProcedureReview",
        "reviewBodyChallenge",
        "legalProcedures"
})
public class ReviewProceedings {
    @JsonProperty("buyerProcedureReview")
    @JsonPropertyDescription("A True/False field to indicate if an economic operator applied to the buyer for a " +
            "review of the procedure. Required by the EU")
    private final Boolean buyerProcedureReview;

    @JsonProperty("reviewBodyChallenge")
    @JsonPropertyDescription("A True/False field to indicate if an economic operator or another party challenged the " +
            "procedure before a review body. Required by the EU")
    private final Boolean reviewBodyChallenge;

    @JsonProperty("legalProcedures")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("Identifier(s) of any review procedure(s) initiated. Required by the EU")
    private final Set<LegalProceedings> legalProcedures;

    public ReviewProceedings(@JsonProperty("buyerProcedureReview") final Boolean buyerProcedureReview,
                             @JsonProperty("reviewBodyChallenge") final Boolean reviewBodyChallenge,
                             @JsonProperty("legalProcedures") final LinkedHashSet<LegalProceedings> legalProcedures) {
        this.buyerProcedureReview = buyerProcedureReview;
        this.reviewBodyChallenge = reviewBodyChallenge;
        this.legalProcedures = legalProcedures;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(buyerProcedureReview)
                .append(reviewBodyChallenge)
                .append(legalProcedures)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ReviewProceedings)) {
            return false;
        }
        final ReviewProceedings rhs = (ReviewProceedings) other;
        return new EqualsBuilder().append(buyerProcedureReview, rhs.buyerProcedureReview)
                .append(reviewBodyChallenge, rhs.reviewBodyChallenge)
                .append(legalProcedures, rhs.legalProcedures)
                .isEquals();
    }
}
