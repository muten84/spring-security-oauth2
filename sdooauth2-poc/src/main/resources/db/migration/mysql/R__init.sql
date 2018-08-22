create table if not exists city (id bigint not null, country varchar(255) not null, map varchar(255) not null, name varchar(255) not null, state varchar(255) not null, primary key (id));
create table  if not exists city_sequence (next_val bigint);
insert into city_sequence values ( 1 );
