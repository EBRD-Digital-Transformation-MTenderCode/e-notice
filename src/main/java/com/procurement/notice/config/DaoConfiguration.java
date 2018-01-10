package com.procurement.notice.config;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.procurement.notice.config.properties.CassandraProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CassandraProperties.class})
public class DaoConfiguration {

    private final CassandraProperties properties;

    @Autowired
    public DaoConfiguration(final CassandraProperties cassandraProperties) {
        this.properties = cassandraProperties;
    }

    @Bean
    public Session session() {
        final Cluster cluster = getCluster();
        cluster.init();
        return cluster.connect(properties.getKeyspace());
    }

    Cluster getCluster() {
        final PoolingOptions poolingOptions = new PoolingOptions()
                .setMaxConnectionsPerHost(HostDistance.LOCAL, 10);

        return Cluster.builder()
                .addContactPoint(properties.getContactPoint())
                .withProtocolVersion(ProtocolVersion.NEWEST_SUPPORTED)
                .withPoolingOptions(poolingOptions)
                .withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy()))
                .withAuthProvider(new PlainTextAuthProvider(properties.getUsername(), properties.getPassword()))
                .build();
    }
}
