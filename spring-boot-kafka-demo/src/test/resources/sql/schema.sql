create table products (
    id SERIAL PRIMARY KEY,
    code varchar not null UNIQUE,
    name varchar not null,
    price numeric(5,2) not null
);