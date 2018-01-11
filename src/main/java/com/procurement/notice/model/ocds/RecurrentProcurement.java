package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isRecurrent",
        "dates",
        "description"
})
public class RecurrentProcurement {
    @JsonProperty("isRecurrent")
    @JsonPropertyDescription("A True/False field to indicate whether this is a recurrent procurement")
    private final Boolean isRecurrent;

    @JsonProperty("dates")
    @JsonPropertyDescription("Estimated date(s) for subsequent call(s) for competition")
    private final List<Period> dates;

    @JsonProperty("description")
    @JsonPropertyDescription("Any further information about subsequent call(s) for competition.")
    private final String description;

    @JsonCreator
    public RecurrentProcurement(@JsonProperty("isRecurrent") final Boolean isRecurrent,
                                @JsonProperty("dates") final List<Period> dates,
                                @JsonProperty("description") final String description) {
        this.isRecurrent = isRecurrent;
        this.dates = dates;
        this.description = description;
    }
}
