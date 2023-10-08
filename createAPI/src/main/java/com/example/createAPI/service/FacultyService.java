package com.example.createAPI.service;

import com.example.createAPI.exception.FacultyNotFoundException;
import com.example.createAPI.exception.StudentNotFoundException;
import com.example.createAPI.model.Faculty;
import com.example.createAPI.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty getById(Long id){
        logger.info("running metod getById");
        return facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    public Faculty create(Faculty faculty){
        logger.info("running metod create");
        return facultyRepository.save(faculty);
    }

    public Faculty update(Long id, Faculty faculty) {
        logger.info("running metod update");
        Faculty existingFaculty = facultyRepository.findById(id)
                        .orElseThrow(FacultyNotFoundException::new);
        existingFaculty.setName(faculty.getName());
        existingFaculty.setColor(faculty.getColor());
        return facultyRepository.save(existingFaculty);
    }

    public Faculty remove(Long id){
        logger.info("running metod remove");
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(FacultyNotFoundException::new);
        facultyRepository.delete(faculty);
        return faculty;
    }

    public Collection<Faculty> getByColor(String color){
        logger.info("running metod getByColor");
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> findAllByColorIgnoreCaseOrNameIgnoreCase(String name, String color){
        logger.info("running metod findAllByColorIgnoreCaseOrNameIgnoreCase");
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(name, color);
    }

    public Collection<Faculty> getAll(){
        logger.info("running metod getAll");
        return facultyRepository.findAll();
    }

    public Faculty getByStudentId(Long studentId){
        logger.info("running metod getByStudentId");
        return facultyRepository.findByStudentId(studentId).orElseThrow(FacultyNotFoundException::new);
    }
}
