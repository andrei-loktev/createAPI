package com.example.createAPI.service;

import com.example.createAPI.exception.StudentNotFoundException;
import com.example.createAPI.model.Faculty;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Service
public class FacultyService {
    private Map<Long, Faculty> map = new HashMap<>();
    private Long COUNTER = 1L;

    public Faculty getById(Long id){
        return map.get(id);
    }

    public Collection<Faculty> getByColor(String color){
        return map.values().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .toList();
    }
    public Collection<Faculty> getAll(){
        return map.values();
    }
    public Faculty create(Faculty faculty){
        Long nextId = COUNTER++;
        faculty.setId(nextId);
        map.put(faculty.getId(), faculty);
        return faculty;
    }
    public Faculty update(Long id, Faculty faculty) {
        if (!map.containsKey(id)) {
            throw new StudentNotFoundException();
        }
        Faculty existingFaculty = map.get(id);
        existingFaculty.setName(faculty.getName());
        existingFaculty.setColor(faculty.getColor());
        return existingFaculty;
    }
    public void delete(Long id){
        if(!map.containsKey(id)){
            throw new StudentNotFoundException();
        }
        map.remove(id);
    }
}
