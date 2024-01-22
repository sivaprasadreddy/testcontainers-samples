create table products (
    id bigserial primary key,
    code varchar not null unique,
    name varchar not null,
    price numeric(5,2) not null
);

insert into products(code, name, price) values('P100', 'Product One', 35.0);
insert into products(code, name, price) values('P200', 'Product Two', 15.0);
