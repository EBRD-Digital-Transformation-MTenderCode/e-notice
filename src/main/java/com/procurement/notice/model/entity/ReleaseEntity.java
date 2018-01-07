package com.procurement.notice.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

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

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}


