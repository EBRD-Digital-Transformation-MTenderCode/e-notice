CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.notice_release (
    cp_id text,
    oc_id text,
    release_date timestamp,
    release_id text,
    stage text,
    json_data text,
    primary key(cp_id, oc_id, release_date)
)WITH CLUSTERING ORDER BY (oc_id ASC, release_date ASC);

SELECT * FROM ocds.notice_release;

TRUNCATE ocds.notice_release;

CREATE TABLE IF NOT EXISTS ocds.notice_compiled_release (
       cp_id text,
       oc_id text,
       release_date timestamp,
       release_id text,
       stage text,
       json_data text,
       primary key(cp_id, oc_id)
)WITH CLUSTERING ORDER BY (oc_id ASC);

SELECT * FROM ocds.notice_compiled_release;

TRUNCATE ocds.notice_compiled_release;


CREATE TABLE IF NOT EXISTS  ocds.notice_offset (
    cp_id text,
    release_date timestamp,
    primary key(cp_id)
);

CREATE INDEX date_index ON ocds.notice_offset (release_date);

TRUNCATE ocds.notice_offset;

SELECT * FROM ocds.notice_offset;

SELECT * FROM ocds.notice_offset WHERE release_date < '2017-12-11 18:31:51' AND release_date > '2017-12-11 18:31:00' LIMIT 2 ALLOW FILTERING;