package com.example.createAPI.controller;

import com.example.createAPI.model.Faculty;
import com.example.createAPI.model.Student;
import com.example.createAPI.repository.FacultyRepository;
import com.example.createAPI.repository.StudentRepository;
import com.example.createAPI.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    StudentRepository studentRepository;

    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    StudentService studentService;

    @Test
    void getById() throws Exception {
        Student student = new Student(1L, "Katya", 30);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1")  //создаём запрос
                        .accept(MediaType.APPLICATION_JSON)  //запрос принимает JSON
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$.name").value("Katya"));
    }

    @Test
    void create() throws Exception {
        Student student = new Student(1L, "Katya", 30);
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/")
                        .content(objectMapper.writeValueAsString(student))  //преобразование объекта в JSON
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$.name").value("Katya"));
    }

    @Test
    void update() throws Exception {
        Student student = new Student(1L, "Katya", 30);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/1")
                        .content(objectMapper.writeValueAsString(student))  //преобразование объекта в JSON
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$.name").value("Katya"));
    }

    @Test
    void delete() throws Exception {
        Student student = new Student(1L, "Katya", 30);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1")
                        .content(objectMapper.writeValueAsString(student))  //преобразование объекта в JSON
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void filteredByAge() throws Exception {
        when(studentRepository.findAllByAgeBetween(20, 30))
                .thenReturn(Arrays.asList(
                        new Student(1L, "Katya", 30),
                        new Student(2L, "Olya", 25)));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/byAge?min=20&max=30")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Katya"))
                .andExpect(jsonPath("$[1].name").value("Olya"));

    }

    @Test
    void getByFacultyId() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "Katya", 30),
                new Student(2L, "Olya", 25));
        Faculty faculty = new Faculty(1L, "math", "red");
        faculty.setStudent(students);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/byFaculty?facultyId=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

    }
}
