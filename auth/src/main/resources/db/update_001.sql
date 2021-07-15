create table person (
    id serial primary key not null,
    login varchar(2000),
    password varchar(2000)
);

insert into person (login, password) values ('Petr Bogomolov', '123');
insert into person (login, password) values ('admin', 'admin');
insert into person (login, password) values ('user', 'user');
