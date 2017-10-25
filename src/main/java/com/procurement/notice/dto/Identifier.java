package com.procurement.notice.dto;

import lombok.Data;

@Data
public class Identifier {
    public String scheme;
    public String id;
    public String legalName;
    public String uri;
}