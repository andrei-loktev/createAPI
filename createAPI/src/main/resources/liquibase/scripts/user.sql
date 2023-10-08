-- liquibase formatted sql

-- changeset andrey:student
CREATE INDEX student_name_index ON student (name);

-- changeset andrey:faculty
CREATE INDEX faculty_index ON faculty (name, color);
