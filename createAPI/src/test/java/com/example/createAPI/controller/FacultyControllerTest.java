package com.example.createAPI.controller;

import com.example.createAPI.CreateApiApplication;
import com.example.createAPI.model.Faculty;
import com.example.createAPI.repository.FacultyRepository;
import com.example.createAPI.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = CreateApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @Autowired
    TestRestTemplate template;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    StudentRepository studentRepository;

    @AfterEach
    void clear() {
        facultyRepository.deleteAll();
    }

    @Test
    void create() {
        String name = "math";
        String color = "red";

        ResponseEntity<Faculty> response = createFaculty(name, color);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
    }

    @Test
    void update() {
        ResponseEntity<Faculty> response = createFaculty("math", "red");  //создали запись
        Long facultyId = response.getBody().getId();  // получили id факультета
        template.put("/faculty/" + facultyId, new Faculty(null, "math", "green"));
        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("green");
    }

    @Test
    void getById() {
        ResponseEntity<Faculty> response = createFaculty("math", "red");  //создали запись
        Long facultyId = response.getBody().getId();  // получили id факультета

        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("red");
        assertThat(response.getBody().getName()).isEqualTo("math");
    }

    @Test
    void delete() {
        ResponseEntity<Faculty> response = createFaculty("math", "red");  //создали запись
        Long facultyId = response.getBody().getId();  // получили id факультета

        template.delete("/faculty/" + facultyId);
        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAll() {
        createFaculty("math", "red");
        createFaculty("fiz", "blue");
        ResponseEntity<Collection> response = template.getForEntity("/faculty", Collection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void getByColorOrName() {
        String name = "math";
        createFaculty(name, "red");
        createFaculty("fiz", "blue");
        ResponseEntity<ArrayList> response = template
                .getForEntity("/faculty/byNameOrColor?nameOrColor=" + name, ArrayList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String, String> element = (HashMap) response.getBody().iterator().next();
        assertThat(element.get("name")).isEqualTo(name);
    }

    private ResponseEntity<Faculty> createFaculty(String name, String color) {
        ResponseEntity<Faculty> response = template.postForEntity("/faculty",
                new Faculty(null, name, color),
                Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }
}
