package com.procurement.notice.model.dto;

import lombok.Data;

@Data
public class ElectronicWorkFlows {
    public Boolean useOrdering;
    public Boolean usePayment;
    public Boolean acceptInvoicing;
}
