create sequence customer_id_seq start with 1 increment by 50;
create sequence product_id_seq start with 1 increment by 50;

create table customers
(
    id    bigint DEFAULT nextval('customer_id_seq') not null,
    email varchar(255)                              not null,
    name  varchar(255)                              not null,
    primary key (id)
);

create table products
(
    id          bigint  DEFAULT nextval('product_id_seq') not null,
    name        varchar(255)                              not null,
    description varchar(255),
    price       numeric                                   not null,
    disabled    boolean default false,
    primary key (id)
);