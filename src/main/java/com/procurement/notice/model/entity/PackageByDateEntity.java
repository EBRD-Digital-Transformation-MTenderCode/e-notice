package com.procurement.notice.model.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@Table("notice_package_by_date")
public class PackageByDateEntity {

    @PrimaryKeyColumn(name = "day_date", type = PrimaryKeyType.PARTITIONED)
    private String day_date;

    @PrimaryKeyColumn(name = "release_date", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime releaseDate;

    @Column(value = "cp_id")
    private String cpId;

    @Column(value = "oc_id")
    private String ocId;

    @Column(value = "release_id")
    private String releaseId;
}
