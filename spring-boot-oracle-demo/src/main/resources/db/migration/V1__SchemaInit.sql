create table customers
(
    id    NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    email varchar(255) not null,
    name  varchar(255) not null
);

create table products
(
    id          NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    name        varchar(255) not null,
    description varchar(255),
    price       numeric      not null,
    disabled    NUMBER(1,0) DEFAULT 0
);