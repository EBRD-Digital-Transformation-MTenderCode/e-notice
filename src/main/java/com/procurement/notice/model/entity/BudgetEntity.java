package com.procurement.notice.model.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetEntity {

    private String cpId;

    private String ocId;

    private Date releaseDate;

    private String releaseId;

    private String stage;

    private Double amount;

    private String jsonData;
}


