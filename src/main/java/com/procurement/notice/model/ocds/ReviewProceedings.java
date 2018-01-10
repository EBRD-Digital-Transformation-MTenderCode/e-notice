package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;

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
}
