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
        "transactions",
        "milestones",
        "documents"
})
public class Implementation {
    @JsonProperty("transactions")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("A list of the spending transactions made against this contract")
    private final Set<Transaction> transactions;

    @JsonProperty("milestones")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("As milestones are completed, milestone completions should be documented.")
    private final Set<Milestone> milestones;

    @JsonProperty("documents")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("Documents and reports that are part of the implementation phase e.g. audit and " +
            "evaluation reports.")
    private final Set<Document> documents;

    @JsonCreator
    public Implementation(@JsonProperty("transactions") final LinkedHashSet<Transaction> transactions,
                          @JsonProperty("milestones") final LinkedHashSet<Milestone> milestones,
                          @JsonProperty("documents") final LinkedHashSet<Document> documents) {
        this.transactions = transactions;
        this.milestones = milestones;
        this.documents = documents;
    }
}
