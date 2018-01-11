package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "hasDynamicPurchasingSystem",
        "hasOutsideBuyerAccess",
        "noFurtherContracts"
})
public class DynamicPurchasingSystem {
    @JsonProperty("hasDynamicPurchasingSystem")
    @JsonPropertyDescription("A True/False field to indicate whether a Dynamic Purchasing System has been set up.")
    private final Boolean hasDynamicPurchasingSystem;

    @JsonProperty("hasOutsideBuyerAccess")
    @JsonPropertyDescription("A True/False field to indicate whether the Dynamic Purchasing System may be used by " +
            "buyers outside the notice.")
    private final Boolean hasOutsideBuyerAccess;

    @JsonProperty("noFurtherContracts")
    @JsonPropertyDescription("A True/False field to indicate whether no further contracts will be awarded in this " +
            "dynamic purchasing system.")
    private final Boolean noFurtherContracts;

    @JsonCreator
    public DynamicPurchasingSystem(@JsonProperty("hasDynamicPurchasingSystem") final Boolean hasDynamicPurchasingSystem,
                                   @JsonProperty("hasOutsideBuyerAccess") final Boolean hasOutsideBuyerAccess,
                                   @JsonProperty("noFurtherContracts") final Boolean noFurtherContracts) {
        this.hasDynamicPurchasingSystem = hasDynamicPurchasingSystem;
        this.hasOutsideBuyerAccess = hasOutsideBuyerAccess;
        this.noFurtherContracts = noFurtherContracts;
    }
}
