package com.example.createAPI.service;

import com.example.createAPI.exception.StudentNotFoundException;
import com.example.createAPI.model.Student;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private Map<Long, Student> map = new HashMap<>();
    private Long COUNTER = 1L;

    public Student getById(Long id) {
        return map.get(id);
    }

    public Collection<Student> getAll() {
        return map.values();
    }

    public Student create(Student student) {
        Long nextId = COUNTER++;
        student.setId(nextId);
        map.put(student.getId(), student);
        return student;
    }

    public Student update(Long id, Student student) {
        if (!map.containsKey(id)) {
            throw new StudentNotFoundException();
        }
        Student existingStudent = map.get(id);
        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        return existingStudent;
    }

    public void delete(Long id) {
        if (!map.containsKey(id)) {
            throw new StudentNotFoundException();
        }
        map.remove(id);
    }
}
