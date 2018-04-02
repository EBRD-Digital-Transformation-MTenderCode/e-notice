package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "relationship",
        "scheme",
        "identifier",
        "uri"
})
public class RelatedProcess {
    @JsonProperty("id")
    @JsonPropertyDescription("A local identifier for this relationship, unique within this array.")
    @NotNull
    private final String id;

    @JsonProperty("relationship")
    @JsonPropertyDescription("Specify the type of relationship using the [related process codelist](http://standard" +
            ".open-contracting.org/latest/en/schema/codelists/#related-process).")
    private final List<RelatedProcessType> relationship;

    @JsonProperty("scheme")
    @JsonPropertyDescription("The identification scheme used by this cross-reference from the [related process scheme" +
            " codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#related-process-scheme) codelist" +
            ". When cross-referencing information also published using OCDS, an Open Contracting ID (ocid) should be used.")
    private final RelatedProcessScheme scheme;

    @JsonProperty("identifier")
    @JsonPropertyDescription("The identifier of the related process. When cross-referencing information also " +
            "published using OCDS, this should be the Open Contracting ID (ocid).")
    private final String identifier;

    @JsonProperty("uri")
    @JsonPropertyDescription("A URI pointing to a machine-readable document, release or record package containing the" +
            " identified related process.")
    private final String uri;

    @JsonCreator
    public RelatedProcess(@JsonProperty("id") final String id,
                          @JsonProperty("relationship") final List<RelatedProcessType> relationship,
                          @JsonProperty("scheme") final RelatedProcessScheme scheme,
                          @JsonProperty("identifier") final String identifier,
                          @JsonProperty("uri") final String uri) {
        this.id = id;
        this.relationship = relationship;
        this.scheme = scheme;
        this.identifier = identifier;
        this.uri = uri;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(relationship)
                .append(scheme)
                .append(identifier)
                .append(uri)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RelatedProcess)) {
            return false;
        }
        final RelatedProcess rhs = (RelatedProcess) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(relationship, rhs.relationship)
                .append(scheme, rhs.scheme)
                .append(identifier, rhs.identifier)
                .append(uri, rhs.uri)
                .isEquals();
    }


}
