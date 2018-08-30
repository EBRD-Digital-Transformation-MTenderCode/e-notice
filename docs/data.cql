CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.notice_release (
    cp_id text,
    oc_id text,
    release_date timestamp,
    release_id text,
    stage text,
    json_data text,
    primary key(cp_id, oc_id, release_id)
)WITH CLUSTERING ORDER BY (oc_id ASC);

CREATE TABLE IF NOT EXISTS ocds.notice_compiled_release (
       cp_id text,
       oc_id text,
       release_date timestamp,
       release_id text,
       stage text,
       status text,
       json_data text,
       primary key(cp_id, oc_id)
)WITH CLUSTERING ORDER BY (oc_id ASC);

CREATE TABLE IF NOT EXISTS  ocds.notice_offset (
    cp_id text,
    release_date timestamp,
    status text,
    stage text,
    primary key(cp_id)
);

CREATE TABLE IF NOT EXISTS  ocds.notice_budget_release (
    cp_id text,
    oc_id text,
    release_date timestamp,
    release_id text,
    stage text,
    json_data text,
    primary key(cp_id, oc_id, release_id)
)WITH CLUSTERING ORDER BY (oc_id ASC);

CREATE TABLE IF NOT EXISTS ocds.notice_budget_compiled_release (
       cp_id text,
       oc_id text,
       release_date timestamp,
       release_id text,
       stage text,
       amount decimal,
       json_data text,
       primary key(cp_id, oc_id)
)WITH CLUSTERING ORDER BY (oc_id ASC);

CREATE TABLE IF NOT EXISTS  ocds.notice_budget_offset (
    cp_id text,
    release_date timestamp,
    primary key(cp_id)
);



