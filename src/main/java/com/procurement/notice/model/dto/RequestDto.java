package com.procurement.notice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class RequestDto {

    @JsonProperty(value = "cpid")
    @NonNull
    private String cpId;

    @JsonProperty(value = "ocid")
    @NonNull
    private String ocId;

    @JsonProperty(value = "stage")
    @NonNull
    private String stage;

    @JsonProperty(value = "tag")
    @NonNull
    private List<String> tag;

    @JsonProperty(value = "initiationType")
    @NonNull
    private String initiationType;

    @JsonProperty(value = "language")
    @NonNull
    private String language;

    @JsonProperty(value = "jsonData")
    @NonNull
    private JsonNode jsonData;
}
