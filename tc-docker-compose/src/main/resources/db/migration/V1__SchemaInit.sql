create sequence product_id_seq start with 1 increment by 50;

create table products
(
    id    bigint DEFAULT nextval('product_id_seq') not null,
    code  varchar(255)                             not null,
    name  varchar(255),
    price numeric(5, 2)                            not null,
    primary key (id),
    constraint code_unique unique (code)
);
