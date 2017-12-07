package com.procurement.notice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@ComponentScan(basePackages = "com.procurement.notice.model.entity")
@EnableCassandraRepositories(basePackages = "com.procurement.notice.repository")
public class CassandraConfig {
}
