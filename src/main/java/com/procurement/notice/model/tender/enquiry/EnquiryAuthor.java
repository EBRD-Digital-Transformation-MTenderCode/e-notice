package com.procurement.notice.model.tender.enquiry;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.procurement.notice.model.ocds.Address;
import com.procurement.notice.model.ocds.ContactPoint;
import com.procurement.notice.model.ocds.Identifier;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.validation.Valid;
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
public class EnquiryAuthor {
    @JsonProperty("id")
    @JsonPropertyDescription("The id of the party being referenced. This must match the id of an entry in the parties" +
            " section.")
    @NotNull
    private final String id;

    @JsonProperty("name")
    @JsonPropertyDescription("The name of the party being referenced. This must match the name of an entry in the " +
            "parties section.")
    @Size(min = 1)
    @NotNull
    private final String name;

    @JsonCreator
    public EnquiryAuthor(@JsonProperty("id") final String id,
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
        if (!(other instanceof EnquiryAuthor)) {
            return false;
        }
        final EnquiryAuthor rhs = (EnquiryAuthor) other;
        return new EqualsBuilder().append(name, rhs.name)
                .append(id, rhs.id)
                .isEquals();
    }
}
