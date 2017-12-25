CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.notice_release (
    cp_id text,
    oc_id text,
    release_date timestamp,
    release_id text,
    stage text,
    json_data text,
    primary key(cp_id, oc_id, release_date, release_id)
)WITH CLUSTERING ORDER BY (oc_id DESC, release_date DESC);

SELECT * FROM ocds.notice_release;

SELECT release_version FROM ocds.notice_release WHERE cp_id = 'ocds-213czf-000-00001' AND oc_id = 'ocds-213czf-000-00001-01';

SELECT * FROM ocds.notice_release WHERE release_date >= '2017-12-11 16:40:00';

select release_version from notice_release where cp_id=?0 and oc_id = ?1 LIMIT 1

TRUNCATE ocds.notice_release;


CREATE TABLE IF NOT EXISTS  ocds.notice_package_by_date (
    day_date text,
    release_date timestamp,
    cp_id text,
    oc_id text,
    release_id text,
    primary key(day_date, release_date)
)WITH CLUSTERING ORDER BY (release_date DESC);

SELECT * FROM ocds.notice_package_by_date WHERE day_date = '2017-12-12' AND release_date > '2017-12-12 09:17:58+0000';