/* Setting up PATIENT DB */
drop database if exists patient ;

create database patient;
use patient;

create table p_patient(
p_id int PRIMARY KEY AUTO_INCREMENT,
p_last_name varchar(20) NOT NULL,
p_first_name varchar(20) NOT NULL,
p_birth_date char(10) NOT NULL,
p_gender char(1) NOT NULL,
p_address varchar(40),
p_phone_number char(12) );

insert into p_patient(p_last_name,p_first_name,p_birth_date,p_gender,p_address,p_phone_number) values('TestNone','Test','1966-12-31','F','1 Brookside St','100-222-3333');
insert into p_patient(p_last_name,p_first_name,p_birth_date,p_gender,p_address,p_phone_number) values('TestBorderline','Test','1945-06-24','M','2 High St','200-333-4444');
insert into p_patient(p_last_name,p_first_name,p_birth_date,p_gender,p_address,p_phone_number) values('TestInDanger','Test','2004-06-18','M','3 Club Road','300-444-5555');
insert into p_patient(p_last_name,p_first_name,p_birth_date,p_gender,p_address,p_phone_number) values('TestEarlyOnset','Test','2002-06-28','F','4 Valley Dr','400-555-6666');
insert into p_patient(p_last_name,p_first_name,p_birth_date,p_gender,p_address,p_phone_number) values('TestTest','Test','1994-01-01','F','5 East Bd','500-666-7777');
commit;
