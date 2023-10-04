-- Возраст студента не может быть меньше 16 лет.
alter table student
add constraint age_constraint check (age<16);
-- Имена студентов должны быть уникальными и не равны нулю.
alter table student
add constraint uniq_name unique (name);

alter table student
add column name is not null;

-- Пара “значение названия” - “цвет факультета” должна быть уникальной.
alter table faculty
add constraint uniq_name_color unique (name, color)

-- При создании студента без возраста ему автоматически должно присваиваться 20 лет.
alter table student
add column age set default 20;