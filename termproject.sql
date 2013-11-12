--CS1555 Term Project
-- ### 1. Create the tables and insert necessary data

drop table flight cascade constraints ;
drop table administrator cascade constraints ;
drop table product cascade constraints ;
drop table bidlog cascade constraints ;
drop table category cascade constraints ;
drop table belongsto cascade constraints ;

drop table sys_time cascade constraints ;

drop sequence seq1;
drop sequence seq2;

create table flight(
flight_number varchar(3), 	
plane_type char(4),
departure_city varchar(3),
arrival_city varchar(4),
departure_time varchar(4),
arrival_time varchar(4),
weekly_schedule varchar(7)
);

create table plane(
plane_type char(4),
manufacture varchar(10),
plane capacity int,
last service date,
year int
);

create table price(
departure city varchar(3),
arrival city varchar(3),
high price int,
low price int
);

create table customer(
cid varchar(9),
salutation varchar(3),
first_name varchar(30),
last_name varchar(30),
credit_card num varchar(16),
credit_card expire date,
street varchar(30),
city varchar(30),
state varchar(2),
phone varchar(10),
email varchar(30)
);

create table reservation(
reservation_number varchar(5)
cid varchar(9)
cost int
reservation_date date
ticketed varchar(1)
);

create table reservation_detail(
reservation_number varchar(5)
fight_number varchar(3)
fight_date date
leg int
);

create table Date(
c_date date
);


alter table flight add constraint pk_flight primary key(flight_number) ;
alter table plane add constraint pk_plane primary key(plane_type) ;
alter table price add constraint pk_price primary key(departure city, arrival city) ;
alter table customer add constraint pk_customer primary key(cid) ;
alter table reservation add constraint pk_reservation primary key(reservation_number) ;
alter table date add constraint pk_date primary key(auction_id, category) ;

alter table flight add constraint fk_flight foreign key(plane_type) references plane(plane_type) ;
alter table reservation add constraint fk_reservation foreign key(cid) references customer(cid) ;
alter table reservation add constraint fk_reservation_detail1 foreign key(reservation_number) references reservation(reservation_number) ;
alter table reservation add constraint fk_reservation_detail2 foreign key(flight_number) references flight(flight_number) ;
