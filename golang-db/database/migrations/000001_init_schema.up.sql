create sequence bookmark_id_seq start with 1 increment by 1;

create table bookmarks
(
    id         bigint DEFAULT nextval('bookmark_id_seq') not null,
    url        varchar(1024)                         not null,
    title      varchar(1024),
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
