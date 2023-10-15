package com.example.createAPI.controller;

import com.example.createAPI.model.Faculty;
import com.example.createAPI.model.Student;
import com.example.createAPI.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {

        this.studentService = studentService;
    }

    @GetMapping
    public Collection<Student> getAll() {

        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable("id") Long id) {

        return studentService.getById(id);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        studentService.delete(id);
    }

    @GetMapping("/filtered")
    public Collection<Student> getByAge(@RequestParam("age") int age) {
        return studentService.getByAge(age);
    }

    @GetMapping("/byAge")
    public Collection<Student> findAllByAgeBetween(@RequestParam("min") int min,
                                                   @RequestParam("max") int max) {
        return studentService.findAllByAgeBetween(min, max);
    }

    @GetMapping("/byFaculty")
    public Collection<Student> getByFacultyId(@RequestParam Long facultyId) {
        return studentService.getByFacultyId(facultyId);
    }

    @GetMapping("stream/startWith_A")
    public List<String> startWithA(){
        return studentService.getAllWithStartA();
    }

    @GetMapping("stream/averageAge")
    public double getAverageAge(){
        return studentService.getAverageAge();
    }
}
