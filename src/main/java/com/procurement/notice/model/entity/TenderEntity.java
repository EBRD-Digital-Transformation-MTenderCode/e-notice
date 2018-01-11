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
public class TenderEntity {

    private String cpId;

    private String ocId;

    private Date releaseDate;

    private String releaseId;

    private String stage;

    private String jsonData;


    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}


