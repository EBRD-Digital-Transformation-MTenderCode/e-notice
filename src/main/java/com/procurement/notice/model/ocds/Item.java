package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "description",
        "classification",
        "additionalClassifications",
        "quantity",
        "unit",
        "relatedLot"
})
public class Item {
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier to reference and merge the items by. Must be unique within a given " +
            "array of items.")
    private String id;

    @JsonProperty("description")
    @JsonPropertyDescription("A description of the goods, services to be provided.")
    private final String description;

    @JsonProperty("classification")
    private final Classification classification;

    @JsonProperty("additionalClassifications")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("An array of additional classifications for the item. See the [itemClassificationScheme]" +
            "(http://standard.open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) codelist " +
            "for " +
            "common options to use in OCDS. This may also be used to present codes from an internal classification " +
            "scheme.")
    private final Set<Classification> additionalClassifications;

    @JsonProperty("quantity")
    @JsonPropertyDescription("The number of units required")
    private final Double quantity;

    @JsonProperty("unit")
    @JsonPropertyDescription("A description of the unit in which the supplies, services or works are provided (e.g. " +
            "hours, kilograms) and the unit-price. For comparability, an established list of units can be used.  ")
    private final Unit unit;

    @JsonProperty("relatedLot")
    @JsonPropertyDescription("If this item belongs to a lot, provide the identifier(s) of the related lot(s) here.")
    private final String relatedLot;

    @JsonCreator
    public Item(@JsonProperty("id") final String id,
                @JsonProperty("description") final String description,
                @JsonProperty("classification") final Classification classification,
                @JsonProperty("additionalClassifications") final LinkedHashSet<Classification>
                        additionalClassifications,
                @JsonProperty("quantity") final Double quantity,
                @JsonProperty("unit") final Unit unit,
                @JsonProperty("relatedLot") final String relatedLot) {
        this.id = id;
        this.description = description;
        this.classification = classification;
        this.additionalClassifications = additionalClassifications;
        this.quantity = quantity;
        this.unit = unit;
        this.relatedLot = relatedLot;
    }
}
