package com.procurement.notice.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrganizationReference {
    public String name;
    public Integer id;
    public Identifier identifier;
    public Address address;
    public List<Identifier> additionalIdentifiers;
    public ContactPoint contactPoint;
}