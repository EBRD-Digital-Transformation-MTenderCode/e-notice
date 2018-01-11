package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "budget",
        "rationale",
        "documents",
        "milestones"
})
public class Planning {
    @JsonProperty("rationale")
    @JsonPropertyDescription("The rationale for the procurement provided in free text. More detail can be provided in" +
            " an attached document.")
    private final String rationale;

    @JsonProperty("budget")
    @JsonPropertyDescription("This section contain information about the budget line, and associated projects, " +
            "through which this contracting process is funded. It draws upon data model of the [Fiscal Data Package]" +
            "(http://fiscal.dataprotocols.org/), and should be used to cross-reference to more detailed information " +
            "held " +
            "using a Budget Data Package, or, where no linked Budget Data Package is available, to provide enough " +
            "information to allow a user to manually or automatically cross-reference with another published source " +
            "of " +
            "budget and project information.")
    private final Budget budget;

    @JsonProperty("documents")
    @JsonPropertyDescription("A list of documents related to the planning process.")
    private final List<Document> documents;

    @JsonProperty("milestones")
    @JsonPropertyDescription("A list of milestones associated with the planning stage.")
    private final List<Milestone> milestones;

    @JsonCreator
    public Planning(@JsonProperty("budget") final Budget budget,
                    @JsonProperty("rationale") final String rationale,
                    @JsonProperty("documents") final List<Document> documents,
                    @JsonProperty("milestones") final List<Milestone> milestones) {
        this.budget = budget;
        this.rationale = rationale;
        this.documents = documents;
        this.milestones = milestones;
    }
}
