create sequence product_id_seq start with 1 increment by 50;

create table products
(
    id          bigint DEFAULT nextval('product_id_seq') not null,
    name        varchar(255)                             not null,
    description varchar(255),
    price       numeric(5, 2)                            not null,
    primary key (id)
);

insert into products(id, name, description, price)
values (1, 'product-1', 'product one', 24.50),
       (2, 'product-2', 'product two', 34.50),
       (3, 'product-3', 'product three', 14.50)
;
