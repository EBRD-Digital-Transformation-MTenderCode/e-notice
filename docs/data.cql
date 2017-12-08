CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.notice_package (
    cp_id text,
    package_date timestamp,
    json_data text,
    primary key(cp_id, package_date)
)WITH CLUSTERING ORDER BY (package_date DESC);

SELECT * FROM ocds.notice_package;
