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
        "requirements"
})
public class RequirementGroup {
    @JsonProperty("description")
    @JsonPropertyDescription("Requirement group description")
    private final String description;
    @JsonProperty("requirements")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("A list requirements which must all be satisified for the requirement group to be met.")
    private final Set<Requirement> requirements;
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier for this requirement group. It must be unique and cannot change within " +
            "the Open Contracting Process it is part of (defined by a single ocid). See the [identifier guidance]" +
            "(http://standard.open-contracting.org/latest/en/schema/identifiers/) for further details.")
    private String id;

    @JsonCreator
    public RequirementGroup(@JsonProperty("id") final String id,
                            @JsonProperty("description") final String description,
                            @JsonProperty("requirements") final LinkedHashSet<Requirement> requirements) {
        this.id = id;
        this.description = description;
        this.requirements = requirements;
    }
}
