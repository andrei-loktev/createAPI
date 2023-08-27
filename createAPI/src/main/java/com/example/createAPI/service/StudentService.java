package com.example.createAPI.service;

import com.example.createAPI.exception.FacultyNotFoundException;
import com.example.createAPI.exception.StudentNotFoundException;
import com.example.createAPI.model.Faculty;
import com.example.createAPI.model.Student;
import com.example.createAPI.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }
    public Collection<Student> getByAge(int age){
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student update(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(FacultyNotFoundException::new);
        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        return studentRepository.save(existingStudent);
    }

    public Student delete(Long id) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
        studentRepository.delete(existingStudent);
        return existingStudent;
    }
}
