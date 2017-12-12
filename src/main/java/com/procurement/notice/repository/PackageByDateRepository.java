package com.procurement.notice.repository;

import com.procurement.notice.model.entity.PackageByDateEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageByDateRepository extends CassandraRepository<PackageByDateEntity, String> {
}
