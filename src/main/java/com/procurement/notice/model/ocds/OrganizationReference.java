package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
    private String id;

    @JsonProperty("name")
    @Size(min = 1)
    @NotNull
    private final String name;

    @JsonProperty("identifier")
    @Valid
    private Identifier identifier;

    @JsonProperty("address")
    @Valid
    private Address address;

    @JsonProperty("additionalIdentifiers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @Valid
    private Set<Identifier> additionalIdentifiers;

    @JsonProperty("contactPoint")
    @Valid
    private ContactPoint contactPoint;

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
