package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isJointProcurement",
        "country"
})
public class JointProcurement {
    @JsonProperty("isJointProcurement")
    @JsonPropertyDescription("A True/False field to indicate if this is a joint procurement or not. Required by the EU")
    private final Boolean isJointProcurement;

    @JsonProperty("country")
    @JsonPropertyDescription("ISO Country Code of the country where the law applies. Use ISO Alpha-2 country codes.")
    private final String country;

    @JsonCreator
    public JointProcurement(@JsonProperty("isJointProcurement") final Boolean isJointProcurement,
                            @JsonProperty("country") final String country) {
        this.isJointProcurement = isJointProcurement;
        this.country = country;
    }
}
