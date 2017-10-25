package com.procurement.notice.dto;

public class Transaction {
    public String id;
    public String source;
    public String date;
    public Value value;
    public OrganizationReference payer;
    public OrganizationReference payee;
    public String uri;
    public Value amount;
    public Identifier providerOrganization;
    public Identifier receiverOrganization;
}
