CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.notice_release (
    cp_id text,
    oc_id text,
    release_date timestamp,
    release_id text,
    stage text,
    json_data text,
    primary key(cp_id, oc_id, release_date, release_id)
)WITH CLUSTERING ORDER BY (oc_id ASC, release_date ASC);

CREATE TABLE IF NOT EXISTS ocds.notice_compiled_release (
       cp_id text,
       oc_id text,
       release_date timestamp,
       release_id text,
       stage text,
       json_data text,
       primary key(cp_id, oc_id, stage)
)WITH CLUSTERING ORDER BY (oc_id ASC);

CREATE TABLE IF NOT EXISTS  ocds.notice_offset (
    cp_id text,
    release_date timestamp,
    primary key(cp_id)
);

CREATE INDEX date_index ON ocds.notice_offset (release_date);

CREATE TABLE IF NOT EXISTS  ocds.notice_budget_release (
    cp_id text,
    oc_id text,
    release_date timestamp,
    release_id text,
    stage text,
    json_data text,
    primary key(cp_id, oc_id, release_date, release_id)
)WITH CLUSTERING ORDER BY (oc_id ASC, release_date ASC);

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

CREATE INDEX budget_date_index ON ocds.notice_budget_offset (release_date);

SELECT * FROM ocds.notice_release;
SELECT * FROM ocds.notice_compiled_release;
SELECT * FROM ocds.notice_offset;
SELECT * FROM ocds.notice_budget_compiled_release;
SELECT * FROM ocds.notice_budget_release;
SELECT * FROM ocds.notice_budget_offset;
SELECT * FROM ocds.notice_budget_offset WHERE release_date < '2017-12-11 18:31:51' AND release_date > '2017-12-11 18:31:00' LIMIT 2 ALLOW FILTERING;

TRUNCATE ocds.notice_release;
TRUNCATE ocds.notice_compiled_release;
TRUNCATE ocds.notice_offset;
TRUNCATE ocds.notice_budget_release;
TRUNCATE ocds.notice_budget_compiled_release;
TRUNCATE ocds.notice_budget_offset;



