package com.procurement.notice.model.tender.ms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name"
})
public class MsOrganizationReference {
    @JsonProperty("id")
    @NotNull
    private final String id;

    @JsonProperty("name")
    @Size(min = 1)
    @NotNull
    private final String name;

    @JsonCreator
    public MsOrganizationReference(@JsonProperty("id") final String id,
                                   @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name)
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MsOrganizationReference)) {
            return false;
        }
        final MsOrganizationReference rhs = (MsOrganizationReference) other;
        return new EqualsBuilder().append(name, rhs.name)
                .append(id, rhs.id)
                .isEquals();
    }
}
