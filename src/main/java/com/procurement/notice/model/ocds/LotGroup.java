package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "relatedLots",
        "optionToCombine",
        "maximumValue"
})
public class LotGroup {
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this group of lots.")
    private final String id;

    @JsonProperty("relatedLots")
    @JsonPropertyDescription("A list of the identifiers of the lots that form this group. Lots may appear in more " +
            "than one group.")
    private final List<String> relatedLots;

    @JsonProperty("optionToCombine")
    @JsonPropertyDescription("The buyer reserves the right to combine the lots in this group when awarding a contract.")
    private final Boolean optionToCombine;

    @JsonProperty("maximumValue")
    private final Value maximumValue;

    @JsonCreator
    public LotGroup(@JsonProperty("id") final String id,
                    @JsonProperty("relatedLots") final List<String> relatedLots,
                    @JsonProperty("optionToCombine") final Boolean optionToCombine,
                    @JsonProperty("maximumValue") final Value maximumValue) {
        this.id = id;
        this.relatedLots = relatedLots;
        this.optionToCombine = optionToCombine;
        this.maximumValue = maximumValue;
    }
}