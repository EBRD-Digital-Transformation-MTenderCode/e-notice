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
        "name",
        "id",
        "identifier",
        "address",
        "additionalIdentifiers",
        "contactPoint"
})
public class OrganizationReference {
    @JsonProperty("id")
    @JsonPropertyDescription("The id of the party being referenced. This must match the id of an entry in the parties" +
            " section.")
    private String id;

    @JsonProperty("name")
    @JsonPropertyDescription("The name of the party being referenced. This must match the name of an entry in the " +
            "parties section.")
    private final String name;

    @JsonProperty("identifier")
    private final Identifier identifier;

    @JsonProperty("address")
    private final Address address;

    @JsonProperty("additionalIdentifiers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("(Deprecated outside the parties section) A list of additional / supplemental " +
            "identifiers for the organization, using the [organization identifier guidance](http://standard" +
            ".open-contracting.org/latest/en/schema/identifiers/). This could be used to provide an internally used " +
            "identifier for this organization in addition to the primary legal entity identifier.")
    private final Set<Identifier> additionalIdentifiers;

    @JsonProperty("contactPoint")
    private final ContactPoint contactPoint;

    @JsonCreator
    public OrganizationReference(@JsonProperty("name") final String name,
                                 @JsonProperty("id") final String id,
                                 @JsonProperty("identifier") final Identifier identifier,
                                 @JsonProperty("address") final Address address,
                                 @JsonProperty("additionalIdentifiers") final LinkedHashSet<Identifier>
                                         additionalIdentifiers,
                                 @JsonProperty("contactPoint") final ContactPoint contactPoint) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.address = address;
        this.additionalIdentifiers = additionalIdentifiers;
        this.contactPoint = contactPoint;
    }
}
