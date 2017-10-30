package com.procurement.notice.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class Organization {
    public String name;
    public String id;
    public Identifier identifier;
    public List<Identifier> additionalIdentifiers;
    public Address address;
    public ContactPoint contactPoint;
    public List<String> roles;
    public Details details;
    public String buyerProfile;
}
