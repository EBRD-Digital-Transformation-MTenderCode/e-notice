package com.procurement.notice.dto;

import lombok.Data;

@Data
public class ChangeableObject {
    public String property;
    public Object former_value;
}
