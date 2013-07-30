drop database if exists erp;

create database erp;

use erp;

create table login
(
	userName varchar(12) not null PRIMARY KEY,
	password varchar(20) not null,
	name varchar(40) not null,
	dob varchar(10) not null,
	yearOfAdmission varchar(4) not null,
	department varchar(30) not null,
	address varchar(30) not null
);


create table superUserLogin
(
	userName varchar(12) not null PRIMARY KEY,
	password varchar(20) not null,
	department varchar(30) not null
);

create table allSubjects
(
	SubjectID varchar(10) not null PRIMARY KEY,
	Subject varchar(40) not null,
	Credits int not null,
	Teacher varchar(30) not null
);

create table 2009s1
(
	userName varchar(12) not null,
	SubjectID varchar(10) not null,
 	Grade varchar(2) not null,
	foreign key(userName) references login(userName),
	foreign key(SubjectID) references allSubjects(SubjectID)
);

create table 2009s2
(
	userName varchar(12) not null,
	SubjectID varchar(10) not null,
 	Grade varchar(2) not null,
	foreign key(userName) references login(userName),
	foreign key(SubjectID) references allSubjects(SubjectID)
);

create table 2010s1
(
	userName varchar(12) not null,
	SubjectID varchar(10) not null,
 	Grade varchar(2) not null,
	foreign key(userName) references login(userName),
	foreign key(SubjectID) references allSubjects(SubjectID)
);

create table 2010s2
(
	userName varchar(12) not null,
	SubjectID varchar(10) not null,
 	Grade varchar(2) not null,
	foreign key(userName) references login(userName),
	foreign key(SubjectID) references allSubjects(SubjectID)
);

create table 2011s1
(
	userName varchar(12) not null,
	SubjectID varchar(10) not null,
 	Grade varchar(2) not null,
	foreign key(userName) references login(userName),
	foreign key(SubjectID) references allSubjects(SubjectID)
);

create table 2011s2
(
	userName varchar(12) not null,
	SubjectID varchar(10) not null,
 	Grade varchar(2) not null,
	foreign key(userName) references login(userName),
	foreign key(SubjectID) references allSubjects(SubjectID)
);

create table utility
(
	column1 varchar(30) not null,
	column2 varchar(30) not null,
	isRegEnabled varchar(5) not null
);

insert into utility(column1, column2) values('currentSemester', '2011s2');

insert into login(userName, password, name, dob, yearOfAdmission, department, address) values('5050', 'user1', 'user1', '31/10/1991', '2009', 'Mathematics', 'heaven city heaven');

insert into superUserLogin(userName, password) values('System', '01010101');

insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('PH11001', 'Physics', 4, 'A.Sinha');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('CS11001', 'Programming and data structure', 4, 'B.Guha');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('CS19001', 'Programming and data structure Lab', 4, 'P.Shekhar');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA31011', 'Object Oriented System Design', 3, 'P.Kumar');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA31009', 'Computer Organization and Architecture', 3, 'DK.Gupta');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA30003', 'Linear Algebra', 3, 'P.Panigrahi');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('CY11001', 'Chemistry', 4, 'A.Das');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('EE11001', 'Electrical Technology', 4, 'P.Pritam');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('HS13001', 'English', 4, 'G.Swami');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('CE13001', 'Engineering Drawing', 3, 'A.Barman');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('ME10001', 'Mechanics', 4, 'S.Nath');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA10002', 'Mathematics-II', 4, 'K.Ghosal');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA10001', 'Mathematics-I', 4, 'K.Josh');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA20103', 'Partial Differebtial Eqn', 3, 'GP.Shekhar');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('HS20001', 'Economics', 4, 'P.Panda');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA21007', 'Design and analysis of algorithm', 3, 'GP.Shekhar');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA20101', 'Transform calculus', 3, 'P.Panigrahi');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('EV20001', 'Environmental science', 2, 'U.Kant');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('EC21101', 'Basic electronics', 4, 'K.Badrinath');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA20104', 'Probablity and statistics', 4, 'K.Sinha');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA20013', 'Discrete Mathematics', 3, 'P.Panigrahi');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA31005', 'Real Analysis', 4, 'N.Gyaneshwar');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('ME19001', 'Intro to manufacturing processes', 2, 'N.Nath');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA30006', 'Switching and finite automata', 4, 'S.Mukhopadhaya');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA41002', 'Modern Algebra', 4, 'C.Nahak');
insert into allSubjects(SubjectID, Subject, Credits, Teacher) values('MA30014', 'Operation Research', 3, 'R.Gayen');

insert into 2009s1(userName, SubjectID, Grade) values('5050', 'CY11001', 'A');
insert into 2009s1(userName, SubjectID, Grade) values('5050', 'HS13001', 'EX');
insert into 2009s1(userName, SubjectID, Grade) values('5050', 'MA10001', 'EX');
insert into 2009s1(userName, SubjectID, Grade) values('5050', 'ME19001', 'EX');

insert into 2009s2(userName, SubjectID, Grade) values('5050', 'CS11001', 'EX');
insert into 2009s2(userName, SubjectID, Grade) values('5050', 'CS19001', 'EX');
insert into 2009s2(userName, SubjectID, Grade) values('5050', 'MA10002', 'EX');
insert into 2009s2(userName, SubjectID, Grade) values('5050', 'PH11001', 'EX');
insert into 2009s2(userName, SubjectID, Grade) values('5050', 'ME10001', 'EX');


insert into 2010s1(userName, SubjectID, Grade) values('5050', 'MA20103', 'EX');
insert into 2010s1(userName, SubjectID, Grade) values('5050', 'MA20101', 'EX');
insert into 2010s1(userName, SubjectID, Grade) values('5050', 'MA21007', 'EX');
insert into 2010s1(userName, SubjectID, Grade) values('5050', 'EV20001', 'EX');
insert into 2010s1(userName, SubjectID, Grade) values('5050', 'HS20001', 'EX');


insert into 2010s2(userName, SubjectID, Grade) values('5050', 'EC21101', 'EX');
insert into 2010s2(userName, SubjectID, Grade) values('5050', 'MA20104', 'EX');
insert into 2010s2(userName, SubjectID, Grade) values('5050', 'MA20013', 'EX');

insert into 2011s1(userName, SubjectID, Grade) values('5050', 'MA31009', 'EX');
insert into 2011s1(userName, SubjectID, Grade) values('5050', 'MA31011', 'A');
insert into 2011s1(userName, SubjectID, Grade) values('5050', 'MA30003', 'EX');
insert into 2011s1(userName, SubjectID, Grade) values('5050', 'MA31005', 'A');

insert into 2011s2(userName, SubjectID) values('5050', 'MA30006');
insert into 2011s2(userName, SubjectID) values('5050', 'MA41002');
insert into 2011s2(userName, SubjectID) values('5050', 'MA30014');

