package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "value",
        "options",
        "recurrentProcurement",
        "renewals",
        "variants"
})
public class Lot {
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this lot, such as a lot number. This is used in relatedLot " +
            "references at the item, document and award level.")
    private String id;

    @JsonProperty("title")
    @JsonPropertyDescription("A title for this lot.")
    private final String title;

    @JsonProperty("description")
    @JsonPropertyDescription("A description of this lot.")
    private final String description;

    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the process related to this lot based on the [tenderStatus " +
            "codelist](http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#tender-status)")
    private final TenderStatus status;

    @JsonProperty("value")
    private final Value value;

    @JsonProperty("options")
    @JsonPropertyDescription("Details about lot options: if they will be accepted and what they can consist of. " +
            "Required by the EU")
    private final List<Option> options;

    @JsonProperty("recurrentProcurement")
    @JsonPropertyDescription("Details of possible recurrent procurements and their subsequent calls for competition.")
    private final List<RecurrentProcurement> recurrentProcurement;

    @JsonProperty("renewals")
    @JsonPropertyDescription("Details of allowable contract renewals")
    private final List<Renewal> renewals;

    @JsonProperty("variants")
    @JsonPropertyDescription("Details about lot variants: if they will be accepted and what they can consist of. " +
            "Required by the EU")
    private final List<Variant> variants;

    @JsonCreator
    public Lot(@JsonProperty("id") final String id,
               @JsonProperty("title") final String title,
               @JsonProperty("description") final String description,
               @JsonProperty("status") final TenderStatus status,
               @JsonProperty("value") final Value value,
               @JsonProperty("options") final List<Option> options,
               @JsonProperty("recurrentProcurement") final List<RecurrentProcurement> recurrentProcurement,
               @JsonProperty("renewals") final List<Renewal> renewals,
               @JsonProperty("variants") final List<Variant> variants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.value = value;
        this.options = options;
        this.recurrentProcurement = recurrentProcurement;
        this.renewals = renewals;
        this.variants = variants;
    }
}
