package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "hasVariants",
        "variantDetails"
})
public class Variant {
    @JsonProperty("hasVariants")
    @JsonPropertyDescription("A True/False field to indicate if lot variants will be accepted. Required by the EU")
    private final Boolean hasVariants;

    @JsonProperty("variantDetails")
    @JsonPropertyDescription("Further information about the lot variants that will be accepted. Required by the EU")
    private final String variantDetails;

    @JsonCreator
    public Variant(@JsonProperty("hasVariants") final Boolean hasVariants,
                   @JsonProperty("variantDetails") final String variantDetails) {
        super();
        this.hasVariants = hasVariants;
        this.variantDetails = variantDetails;
    }
}
