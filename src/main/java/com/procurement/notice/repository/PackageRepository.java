package com.procurement.notice.repository;

import com.procurement.notice.model.entity.PackageEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends CassandraRepository<PackageEntity, String> {

    @Query(value = "select * from notice_package where cp_id=?0 LIMIT 1")
    PackageEntity getLastByCpId(String cpId);
}
