package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title"
})
public class RequirementReference {
    @JsonProperty("id")
    @JsonPropertyDescription("The id of the requirement which the response is applicable to")
    private String id;

    @JsonProperty("title")
    @JsonPropertyDescription("The title of the requirement which the response is applicable to")
    private final String title;

    @JsonCreator
    public RequirementReference(@JsonProperty("id") final String id,
                                @JsonProperty("title") final String title) {
        this.id = id;
        this.title = title;
    }
}
