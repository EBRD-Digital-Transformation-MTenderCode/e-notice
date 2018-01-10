package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "scheme",
        "uid",
        "uri"
})
public class Publisher {
    @JsonProperty("name")
    @JsonPropertyDescription("The name of the organization or department responsible for publishing this data.")
    private final String name;

    @JsonProperty("scheme")
    @JsonPropertyDescription("The scheme that holds the unique identifiers used to identify the item being identified.")
    private final String scheme;

    @JsonProperty("uid")
    @JsonPropertyDescription("The unique ID for this entity under the given ID scheme. Note the use of 'uid' rather " +
            "than 'id'. See issue #245.")
    private final String uid;

    @JsonProperty("uri")
    @JsonPropertyDescription("A URI to identify the publisher.")
    private final URI uri;

    @JsonCreator
    public Publisher(@JsonProperty("name") final String name,
                     @JsonProperty("scheme") final String scheme,
                     @JsonProperty("uid") final String uid,
                     @JsonProperty("uri") final URI uri) {
        this.name = name;
        this.scheme = scheme;
        this.uid = uid;
        this.uri = uri;
    }
}
