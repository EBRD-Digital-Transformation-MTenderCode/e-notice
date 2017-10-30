package com.procurement.notice.model.dto;

import lombok.Data;

@Data
public class ProcedureOutsourcing {
    public Boolean procedureOutsourced;
    public Organization outsourcedTo;
}
