package com.procurement.notice.model.dto;

import lombok.Data;

@Data
public class ChangeableObject {
    public String property;
    public Object former_value;
}
