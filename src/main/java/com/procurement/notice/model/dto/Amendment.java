package com.procurement.notice.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class Amendment {
    public String date;
    public String rationale;
    public String id;
    public String description;
    public String amendsReleaseID;
    public String releaseID;
    public List<ChangeableObject> changes;
}
