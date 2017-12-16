package com.procurement.notice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
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

    @JsonProperty(value = "releaseId")
    @NonNull
    private String releaseId;

    @JsonProperty(value = "releaseDate")
    @NonNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime releaseDate;

    @JsonProperty(value = "jsonData")
    @NonNull
    private JsonNode jsonData;
}
