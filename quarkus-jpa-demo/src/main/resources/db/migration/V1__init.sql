create sequence hibernate_sequence start 1 increment 1;

create table Todo (
   id int8 not null,
   content varchar(255),
   done boolean not null,
   primary key (id)
);

insert into Todo(id, content, done) values (nextval('hibernate_sequence'), 'Learn SpringBoot', true);
insert into Todo(id, content, done) values (nextval('hibernate_sequence'), 'Learn Quarkus', false);
