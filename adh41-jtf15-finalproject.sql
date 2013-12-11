--CS1555 Term Project
-- ### 1. Create the tables and insert necessary data

drop table Flight cascade constraints ;
drop table Plane cascade constraints ;
drop table Price cascade constraints ;
drop table Customer cascade constraints ;
drop table Reservation cascade constraints ;
drop table Reservation_detail cascade constraints ;
drop table Current_d cascade constraints ;

create table Flight(
flight_number varchar2(3), 	
plane_type char(4),
departure_city varchar2(3),
arrival_city varchar2(4),
departure_time varchar2(4),
arrival_time varchar2(4),
weekly_schedule varchar2(7)
);

create table Plane(
plane_type char(4),
manufacture varchar2(10),
plane_capacity int,
last_service date,
year int
);

create table Price(
departure_city varchar2(3),
arrival_city varchar2(3),
high_Price int,
low_Price int
);

create table Customer(
cid varchar2(9),
salutation varchar2(3),
first_name varchar2(30),
last_name varchar2(30),
credit_card_num varchar2(16),
credit_card_expire date,
street varchar2(30),
city varchar2(30),
state varchar2(2),
phone varchar2(10),
email varchar2(30)
);

create table Reservation(
reservation_number varchar2(5),
cid varchar2(9),
cost int,
reservation_date date,
ticketed varchar2(1)
);

create table Reservation_detail(
reservation_number varchar2(5),
flight_number varchar2(3),
flight_date date,
leg int
);

create table Current_d(
c_date date
);

INSERT INTO Flight values('153', 'A320', 'PIT', 'JFK', '1000', '1120', 'SMTWTFS');
INSERT INTO Flight values('154', 'B737', 'JFK', 'DCA', '1230', '1320', 'S-TW-FS');
INSERT INTO Flight values('552', 'E145', 'PIT', 'DCA', '1100', '1150', 'SM-WT-S');

INSERT INTO Plane values('B737', 'Boeing', '125', to_date('09/09/2009','mm/dd/yyyy'), '1996');
INSERT INTO Plane values('A320', 'Airbus', '155', to_date('10/01/2011','mm/dd/yyyy'), '2001');
INSERT INTO Plane values('E145', 'Embraer', '50', to_date('06/15/2010','mm/dd/yyyy'), '2008');

INSERT INTO PRICE values('PIT', 'JFK', '250', '120');
INSERT INTO PRICE values('JFK', 'PIT', '250', '120');
INSERT INTO PRICE values('JFK', 'DCA', '220', '100');
INSERT INTO PRICE values('DCA', 'JFK', '210', '90');
INSERT INTO PRICE values('PIT', 'DCA', '200', '150');
INSERT INTO PRICE values('DCA', 'PIT', '200', '150');

insert into customer values('123456789', 'MR', 'Josh', 'Frey', '4444444444444444', to_date('08/31/1991','mm/dd/yyyy'), 'ward', 'pittsburgh', 'PA', '7174494601', 'jtf15@pitt.edu');


insert into reservation_detail values('12345', '153', to_date('09/09/2009','mm/dd/yyyy'), 1);



CREATE OR REPLACE FUNCTION count_passengers(dflight_number in varchar2) 
return number is
ticket_count number ;
BEGIN


 --Count the passengers who have reserved a flight.
SELECT count(reservation_number) into ticket_count
FROM   reservation_detail
WHERE dflight_number = flight_number;


return ticket_count;
END;

/




CREATE OR REPLACE PROCEDURE add_Customer(
csalutation in varchar2,
cfirst_name in varchar2,
clast_name in varchar2,
ccredit_card_num in varchar2,
ccredit_card_expire in date,
cstreet in varchar2,
ccity  in varchar2,
cstate in varchar2,
cphone in varchar2,
cemail in varchar2)
is
max_cid number ;
new_cid number ;
BEGIN
--Increment the max_cid by one to make a unique new CID for the new customer
SELECT MAX(cid) into max_cid
FROM Customer ;

new_cid := max_cid + 1;
--Insert new customer into the customer table.
insert into Customer 
values(new_cid, csalutation, cfirst_name, clast_name,ccredit_card_num, ccredit_card_expire,
	   cstreet, ccity, cstate, cphone, cemail);


END;
/

CREATE OR REPLACE TRIGGER cancelReservation
AFTER UPDATE ON current_d
FOR EACH ROW
DECLARE
flight_num varchar(3) ;
lowplane_type char(4);
cplane_capacity int ;
low_capac   int ;
BEGIN

--Delete all tuples that have 12 or less hours until their fligh and are unticketed
DELETE FROM reservation CASCADE
WHERE  Reservation_number = (SELECT Reservation_number
FROM reservation_detail r_d 
WHERE (SELECT flight_number 
		FROM flight
		WHERE to_date(departure_time, 'HH24:MI') - to_date(:new.c_date, 'HH24:MI') <= 12) = r_d.flight_number) ;

	--Get the current plane's capacity	
SELECT plane_capacity into cplane_capacity
FROM Plane
WHERE plane_type = (SELECT plane_type
					FROM Flight
					WHERE Flight_number = flight_num) ;
	
--Find the next lowest plane type
SELECT plane_type into lowplane_type
FROM (SELECT plane_type, MAX(Plane_capacity) 
	  FROM Plane
	  WHERE plane_capacity < cplane_capacity);	
	  
--Find the capacity of that plane type
SELECT plane_capacity into low_capac
FROM Plane 
WHERE plane_type = lowplane_type ;

--If the counted passengers after the deletion is leq than the capacity of the 
--next lowst plane, change the plane type of the flight to that
if(count_passengers(flight_num) < low_capac) then
UPDATE Flight SET plane_type = lowplane_type 
WHERE flight_number = flight_num ;
end if ;	

END;
/


CREATE OR REPLACE TRIGGER adjectTicket 
AFTER UPDATE ON Price
FOR EACH ROW
DECLARE
new_high_price int ;
new_low_price int ;
dep_city varchar2(3);
arv_city varchar2(3);
flight_num varchar2(3);
BEGIN
new_high_price := :new.high_price;
new_low_price  := :new.low_price ;
dep_city := :new.departure_city;
arv_city := :new.arrival_city ;
--Get the flight number/s of the flights that are being updated.
SELECT flight_number into flight_num
FROM Flight
WHERE departure_city = dep_city and arrival_city = arv_city;
--Update the prices of the tickets of the flights that are reserved
--but not yet ticketed. I'm not sure how to judge between high and low
--price yet, but I do know that many flights (unless two way) will be high price.
UPDATE Reservation SET cost = new_high_price
WHERE Reservation_number = (SELECT reservation_number FROM reservation_detail WHERE flight_number = flight_num) and ticketed = 'N' ;

END;
/



CREATE OR REPLACE TRIGGER PlaneUpgrade
AFTER INSERT ON Reservation
FOR EACH ROW
DECLARE
cflight_number varchar2(3);
cplane_type char(4);
rowNumb int;
cplane_capacity  int;
new_ptype  char(4);
max_capacity int ;
BEGIN
--Get the flight number of what the new reservation is
SELECT flight_number into cflight_number
FROM reservation_detail r_d
WHERE :new.reservation_number = r_d.reservation_number;
--Get the plane type of the flight
SELECT plane_type into cplane_type
FROM   Flight
WHERE flight_number = cflight_number;
--Find out it's current capacity
SELECT plane_capacity into cplane_capacity 
FROM Plane
WHERE plane_type = cplane_type ;
--Get the max capacity of any plane
--SELECT Max(plane_capacity) into max_capacity
--FROM Plane;
--Get the next biggest  size plane
--SELECT plane_type into cplane_type
--FROM (SELECT plane_type, MIN(Plane_capacity) 
--	  FROM Plane
--	  WHERE plane_capacity > cplane_capacity and  max_capacity != cplane_capacity);

--Get the row number of the current plane type
Select nrow into rowNumb
FROM 
(SELECT Row_Number() OVER(ORDER BY plane_capacity) AS nrow, plane_type
FROM Plane)
WHERE plane_type = cplane_type ;
--Update the new plane type to the row above the current one
SELECT plane_type into new_ptype
FROM (SELECT Row_Number() OVER(ORDER BY plane_capacity) AS nrow, plane_type
FROM Plane)
Where nrow= rowNumb-1;

--if the capacity of the passengers exceeds that of the current plane, upgrade to next biggest.
if(count_passengers(cflight_number) > cplane_capacity) then
UPDATE Flight 
SET plane_type = new_ptype 
WHERE flight_number = cflight_number;
end if;



END;
/

--##ADMINISTRATOR TASKS##--




--##Task 1##--
-- Procedure to delete the database --
-- HANDLED IN SQL/PL --
--##Task 2##--
--Load a schedule
--Use procedure to refrain from having to do multiple inserts in SQL/PL
CREATE OR REPLACE PROCEDURE insertSchedule(
nflight_number in varchar2, 	
nplane_type  in char,
ndeparture_city in varchar2,
narrival_city in varchar2,
ndeparture_time in varchar2,
narrival_time in varchar2,
nweekly_schedule in varchar2)
is
BEGIN
INSERT INTO Flight values(nflight_number, nplane_type, ndeparture_city, narrival_city,
ndeparture_time, narrival_time, nweekly_schedule);
END insertSchedule;
/

--##TASK 3##-
--Similarly to task 2 used to reduce clutter in SQL/PL--

CREATE OR REPLACE PROCEDURE insertPricing(
ndeparture_city in varchar2,
narrival_city in varchar2,
nhigh_Price in int,
nlow_Price in int)
is
BEGIN
INSERT INTO Price values(ndeparture_city, narrival_city, nhigh_price, nlow_price);
END insertPricing;
/

CREATE OR REPLACE PROCEDURE insertPlane(
nplane_type in char,
nmanufacture in varchar2,
nplane_capacity in int,
nlast_service in date,
nyear in int)
is
BEGIN
INSERT INTO Plane values(nplane_type, nplane_type, nplane_capacity, nlast_service, nyear);
END;
/





