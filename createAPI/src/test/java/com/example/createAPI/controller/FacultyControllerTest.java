package com.example.createAPI.controller;

import com.example.createAPI.CreateApiApplication;
import com.example.createAPI.model.Faculty;
import com.example.createAPI.model.Student;
import com.example.createAPI.repository.FacultyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = CreateApiApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @Autowired
    TestRestTemplate template;

    @Autowired
    FacultyRepository facultyRepository;

    @AfterEach
    void clearDb(){
        facultyRepository.deleteAll();
    }

    @Test
    void create(){
        String name = "math";
        String color = "red";

        ResponseEntity<Faculty> response = createFaculty(name, color);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("red");

    }

    @Test
    void getById(){
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        Long facultyId = response.getBody().getId();

        response = template.getForEntity("/faculty/"+facultyId, Faculty.class);  //достали запись из базы данных

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("red");

    }

    @Test
    void byStudent(){
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        Faculty faculty = response.getBody();
        Student student = new Student(null, "Ivan", 20);
        student.setFaculty(faculty);
        ResponseEntity<Student> studentResponse = template
                .postForEntity("/by_student", student, Student.class);
        Long studentId = studentResponse.getBody().getId();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        template.getForEntity("/faculty/by_student?studentId"+ studentId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(faculty);

    }

    @Test
    void getAll(){
        createFaculty("math", "red");
        createFaculty("fiz", "blue");
        ResponseEntity<Collection> response = template
                .getForEntity("/faculty", Collection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);

    }

    @Test
    void delete(){
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        Long facultyId = response.getBody().getId();

        template.delete("/faculty/"+facultyId);
        response = template.getForEntity("/faculty/"+facultyId, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void update(){
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        Long facultyId = response.getBody().getId();

        template.put("/faculty/"+facultyId,new Faculty(null,"math", "blue"));
        response = template.getForEntity("/faculty/"+facultyId, Faculty.class);  //достали запись из базы данных

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("blue");

    }


    private ResponseEntity<Faculty> createFaculty(String name, String color) {
        ResponseEntity<Faculty> response = template.postForEntity("/faculty",
                new Faculty(null, name, color), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }
}
