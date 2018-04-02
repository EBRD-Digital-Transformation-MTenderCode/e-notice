package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "statistics",
        "details"
})
public class Bids {

    @JsonProperty("statistics")
    private final List<BidsStatistic> statistics;

    @JsonProperty("details")
    private List<Bid> details;

    @JsonCreator
    public Bids(@JsonProperty("statistics") final List<BidsStatistic> statistics,
                @JsonProperty("details") final List<Bid> details) {
        this.statistics = statistics;
        this.details = details;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(statistics)
                .append(details)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Bids)) {
            return false;
        }
        final Bids rhs = (Bids) other;
        return new EqualsBuilder().append(statistics, rhs.statistics)
                .append(details, rhs.details)
                .isEquals();
    }
}
