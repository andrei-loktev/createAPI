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
left join avatar a on s.id = a.id;