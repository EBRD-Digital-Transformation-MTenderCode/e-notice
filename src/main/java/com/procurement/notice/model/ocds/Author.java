package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name"
})
public class Author {
    @JsonProperty("id")
    @JsonPropertyDescription("A unique identifier for the author.")
    private String id;

    @JsonProperty("name")
    @JsonPropertyDescription("The name of the author.")
    private final String name;

    @JsonCreator
    public Author(@JsonProperty("id") final String id,
                  @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }
}
