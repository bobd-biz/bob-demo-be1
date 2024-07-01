drop table if exists companies;

drop table if exists companies_seq;

drop table if exists customers;

create table companies (id bigint not null, companyname varchar(50) not null, primary key (id)) engine=InnoDB;

create table companies_seq (next_val bigint) engine=InnoDB;

insert into companies_seq values ( 1 );

create table customers (id varchar(36) not null, companyname varchar(50), firstname varchar(50), lastname varchar(50), primary key (id)) engine=InnoDB;

create index IDXskkto0rs0mcwugts4o6hooj83 on companies (companyname);

alter table companies add constraint UKskkto0rs0mcwugts4o6hooj83 unique (companyname);

create index IDXlpv19ci1ihq0nlhkqwl3dr48k on customers (firstname);

create index IDXqkraisttb4mgpnct4r8y3fdi2 on customers (lastname);

create index IDXf4kuwfmcx0xe67ff7b64e4kw1 on customers (companyname);
