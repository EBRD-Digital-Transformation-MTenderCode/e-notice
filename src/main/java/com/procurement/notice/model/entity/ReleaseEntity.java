package com.procurement.notice.model.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReleaseEntity {

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


