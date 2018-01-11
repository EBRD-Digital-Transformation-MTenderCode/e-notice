package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "startDate",
        "endDate",
        "maxExtentDate",
        "durationInDays"
})
public class Period {
    @JsonProperty("startDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The start date for the period. When known, a precise start date must always be provided.")
    private final LocalDateTime startDate;

    @JsonProperty("endDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The end date for the period. When known, a precise end date must always be provided.")
    private final LocalDateTime endDate;

    @JsonProperty("maxExtentDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The period cannot be extended beyond this date. This field is optional, and can be used" +
            " to express the maximum available data for extension or renewal of this period.")
    private final LocalDateTime maxExtentDate;

    @JsonProperty("durationInDays")
    @JsonPropertyDescription("The maximum duration of this period in days. A user interface may wish to collect or " +
            "display this data in months or years as appropriate, but should convert it into days when completing " +
            "this " +
            "field. This field can be used when exact dates are not known.  Where a startDate and endDate are given, " +
            "this" +
            " field is optional, and should reflect the difference between those two days. Where a startDate and " +
            "maxExtentDate are given, this field is optional, and should reflect the difference between startDate and" +
            " " +
            "maxExtentDate.")
    private final Integer durationInDays;

    @JsonCreator
    public Period(@JsonProperty("startDate") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                      LocalDateTime startDate,
                  @JsonProperty("endDate") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                  LocalDateTime endDate,
                  @JsonProperty("maxExtentDate") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                      LocalDateTime maxExtentDate,
                  @JsonProperty("durationInDays") final Integer durationInDays) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxExtentDate = maxExtentDate;
        this.durationInDays = durationInDays;
    }
}
