package com.example.createAPI.service;

import com.example.createAPI.exception.FacultyNotFoundException;
import com.example.createAPI.exception.StudentNotFoundException;
import com.example.createAPI.model.Faculty;
import com.example.createAPI.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {

        this.facultyRepository = facultyRepository;
    }

    public Faculty getById(Long id){
        return facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    public Faculty create(Faculty faculty){
        return facultyRepository.save(faculty);
    }

    public Faculty update(Long id, Faculty faculty) {
        Faculty existingFaculty = facultyRepository.findById(id)
                        .orElseThrow(FacultyNotFoundException::new);
        existingFaculty.setName(faculty.getName());
        existingFaculty.setColor(faculty.getColor());
        return facultyRepository.save(existingFaculty);
    }

    public Faculty remove(Long id){
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(FacultyNotFoundException::new);
        facultyRepository.delete(faculty);
        return faculty;
    }

    public Collection<Faculty> getByColor(String color){

        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> findAllByColorIgnoreCaseOrNameIgnoreCase(String nameOrColor){
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(nameOrColor);
    }

    public Collection<Faculty> getAll(){
        return facultyRepository.findAll();
    }

    public Faculty getByStudentId(Long studentId){
        return facultyRepository.findByStudentId(studentId).orElseThrow(FacultyNotFoundException::new);
    }
}
