--Описание структуры: у каждого человека есть машина. Причем несколько человек могут пользоваться одной машиной.
--У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет).
--У каждой машины есть марка, модель и стоимость.
--Также не забудьте добавить таблицам первичные ключи и связать их.

create table car (
id serial primary key,
brand varchar,
model varchar,
price int
)

create table driver (
id serial primary key,
name varchar,
age int,
license boolean,
car_id int references car (id)
)

insert into car(brand, model, price) values ('toyota','raize',1000);
insert into car(brand, model, price) values ('daihatsu','rocky',1500);
insert into car(brand, model, price) values ('honda','shuttle',500);

insert into driver(name, age, license, car_id) values ('katya',20,true, 1);
insert into driver(name, age, license, car_id) values ('vika',22,true, 2);
insert into driver(name, age, license, car_id) values ('olya',24,false, 1);
insert into driver(name, age, license, car_id) values ('tanya',26,false , 3);

select * from car;

select * from driver;

select * from car c
inner join driver d on c.id = d.car_id;


--Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах
--(достаточно получить только имя и возраст студента) школы Хогвартс вместе с названиями факультетов.

--Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.

select * from student;
select * from faculty;

select * from student s
left join faculty f on s.id = faculty_student;

select * from avatar;

select student_id from avatar
group by student_id;

select * from student s
inner join avatar a on s.id = a.id;