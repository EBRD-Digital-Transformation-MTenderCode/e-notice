package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "uri"
})
public class LegalProceedings {
    @JsonProperty("id")
    @JsonPropertyDescription("Legal identifier(s) of any review procedure(s) initiated. Required by the EU")
    private final String id;

    @JsonProperty("title")
    @JsonPropertyDescription("Title(s) of any legal proceedings(s) initiated.")
    private final String title;

    @JsonProperty("uri")
    @JsonPropertyDescription("Legal identifier(s) of any review procedure(s) initiated.")
    private final String uri;

    @JsonCreator
    public LegalProceedings(@JsonProperty("id") final String id,
                            @JsonProperty("title") final String title,
                            @JsonProperty("uri") final String uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(title)
                .append(uri)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LegalProceedings)) {
            return false;
        }
        final LegalProceedings rhs = (LegalProceedings) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(title, rhs.title)
                .append(uri, rhs.uri)
                .isEquals();
    }
}
