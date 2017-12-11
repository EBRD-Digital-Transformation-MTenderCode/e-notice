package com.procurement.notice.repository;

import com.procurement.notice.model.entity.ReleaseEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends CassandraRepository<ReleaseEntity, String> {

    @Query(value = "select * from notice_record where cp_id=?0 LIMIT 1")
    ReleaseEntity getLastByCpId(String cpId);
}
