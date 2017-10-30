package com.procurement.notice.model.dto;

import lombok.Data;

@Data
public class Address {
    public String streetAddress;
    public String locality;
    public String region;
    public String postalCode;
    public String countryName;
}