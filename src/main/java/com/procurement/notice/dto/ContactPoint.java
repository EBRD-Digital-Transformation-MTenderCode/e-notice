package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class ContactPoint {
    public String name;
    public String email;
    public String telephone;
    public String faxNumber;
    public String url;
    public List<String> languages;
}