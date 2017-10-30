package com.procurement.notice.model.dto;

import lombok.Data;

@Data
public class RelatedNotice {
    public String id;
    public String scheme;
    public String relationship;
    public String objectOfProcurementInPIN;
    public String uri;
}
