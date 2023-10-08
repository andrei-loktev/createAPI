package com.example.createAPI.service;

import com.example.createAPI.exception.FacultyNotFoundException;
import com.example.createAPI.exception.StudentNotFoundException;
import com.example.createAPI.model.Faculty;
import com.example.createAPI.model.Student;
import com.example.createAPI.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        logger.info("running metod create");
        return studentRepository.save(student);
    }

    public Student getById(Long id) {
        logger.info("running metod getById");
        return studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }
    public Collection<Student> getAll() {
        logger.info("running metod getAll");
        return studentRepository.findAll();
    }

    public Student update(Long id, Student student) {
        logger.info("running metod update");
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(FacultyNotFoundException::new);
        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        return studentRepository.save(existingStudent);
    }

    public Student delete(Long id) {
        logger.info("running metod delete");
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
        studentRepository.delete(existingStudent);
        return existingStudent;
    }

    public Collection<Student> getByAge(int age){
        logger.info("running metod getByAge");
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> findAllByAgeBetween(int min, int max){
        logger.info("running metod findAllByAgeBetween");
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Collection<Student> getByFacultyId(Long facultyId){
        logger.info("running metod getByFacultyId");
        return studentRepository.findAllByFaculty_Id(facultyId);
    }
}
