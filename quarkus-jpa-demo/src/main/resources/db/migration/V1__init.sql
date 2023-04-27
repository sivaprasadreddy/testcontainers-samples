create sequence todo_seq start 1 increment 1;

create table Todo (
   id int8 not null default nextval('todo_seq'),
   content varchar(255),
   done boolean not null,
   primary key (id)
);

insert into Todo(id, content, done) values (nextval('todo_seq'), 'Learn SpringBoot', true);
insert into Todo(id, content, done) values (nextval('todo_seq'), 'Learn Quarkus', false);
