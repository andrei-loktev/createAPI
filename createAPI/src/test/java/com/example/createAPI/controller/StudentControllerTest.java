package com.example.createAPI.controller;

import com.example.createAPI.CreateApiApplication;
import com.example.createAPI.model.Faculty;
import com.example.createAPI.model.Student;
import com.example.createAPI.repository.FacultyRepository;
import com.example.createAPI.repository.StudentRepository;
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
public class StudentControllerTest {

    @Autowired
    TestRestTemplate template;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @AfterEach
    void clearDb(){
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void create() {
        String name = "Katya";
        int age = 25;
        ResponseEntity<Student> studentResponse = createStudent(name, age);

        assertThat(studentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponse.getBody()).isNotNull();
        assertThat(studentResponse.getBody().getName()).isEqualTo("Katya");
        assertThat(studentResponse.getBody().getAge()).isEqualTo(25);
    }

    @Test
    void update(){
        ResponseEntity<Student> response = createStudent("Vika", 27);
        Long studentId = response.getBody().getId();

        template.put("/student/"+studentId,new Student(null,"Vika", 27));
        response = template.getForEntity("/student/"+studentId, Student.class);  //достали запись из базы данных

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Vika");
        assertThat(response.getBody().getAge()).isEqualTo(27);

    }

    @Test
    void delete(){
        ResponseEntity<Student> response = createStudent("Leha", 30);
        Long studentId = response.getBody().getId();

        template.delete("/student/"+studentId);
        response = template.getForEntity("/student/"+studentId, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void getAll(){
        createStudent("Masha", 32);
        createStudent("Anya", 33);
        ResponseEntity<Collection> response = template
                .getForEntity("/student", Collection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);

    }

    @Test
    void ByFacultyId(){
        ResponseEntity<Student> response = createStudent("Petya", 34);
        Student student = response.getBody();
        Faculty faculty = new Faculty(null, "math", "red");
        student.setFaculty(faculty);
        ResponseEntity<Faculty> facultyResponse = template
                .postForEntity("/byFaculty", faculty, Faculty.class);
        Long facultyId = facultyResponse.getBody().getId();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        template.getForEntity("/student/byFaculty?facultyId"+ facultyId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(student);

    }

    private ResponseEntity<Faculty> createFaculty(String name, String color) {
        ResponseEntity<Faculty> response = template
                .postForEntity("/faculty", new Faculty(null, name, color), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }

    private ResponseEntity<Student> createStudent(String name, int age) {
        ResponseEntity<Student> response = template
                .postForEntity("/student", new Student(null, name, age), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }
}
