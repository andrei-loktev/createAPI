package com.example.createAPI.controller;

import com.example.createAPI.model.Faculty;
import com.example.createAPI.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {

        this.facultyService = facultyService;
    }

    @GetMapping
    public Collection<Faculty> getAll() {

        return facultyService.getAll();
    }

    @GetMapping("/{id}")
    public Faculty getById(@PathVariable("id") Long id) {

        return facultyService.getById(id);
    }
    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @PutMapping("/{id}")
    public Faculty update(@PathVariable("id") Long id, @RequestBody Faculty faculty) {
        return facultyService.update(id, faculty);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        facultyService.remove(id);
    }

    @GetMapping("/filtered")
    public Collection<Faculty> getByColor(@RequestParam("color") String color){
        return facultyService.getByColor(color);
    }

    @GetMapping("/byColorName")
    public Collection<Faculty> getByColor(@RequestParam("color") String color,
                                          @RequestParam("name") String name){
        return facultyService.findAllByColorIgnoreCaseOrNameIgnoreCase(name, color);
    }

    @GetMapping("by_student")
    public Faculty getByStudent(@RequestParam Long StudentId){
        return facultyService.getByStudentId(StudentId);
    }
}
