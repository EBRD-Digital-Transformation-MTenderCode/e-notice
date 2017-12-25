package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
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
    private final String id;

    @JsonProperty("description")
    @JsonPropertyDescription("A description of the goods, services to be provided.")
    private final String description;

    @JsonProperty("classification")
    private final Classification classification;

    @JsonProperty("additionalClassifications")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("An array of additional classifications for the item. See the [itemClassificationScheme]" +
            "(http://standard.open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) codelist for " +
            "common options to use in OCDS. This may also be used to present codes from an internal classification scheme.")
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(description)
                .append(classification)
                .append(additionalClassifications)
                .append(quantity)
                .append(unit)
                .append(relatedLot)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Item)) {
            return false;
        }
        final Item rhs = (Item) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(description, rhs.description)
                .append(classification, rhs.classification)
                .append(additionalClassifications, rhs.additionalClassifications)
                .append(quantity, rhs.quantity)
                .append(unit, rhs.unit)
                .append(relatedLot, rhs.relatedLot)
                .isEquals();
    }
}
