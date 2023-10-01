package com.example.createAPI.controller;

import com.example.createAPI.model.Faculty;
import com.example.createAPI.model.Student;
import com.example.createAPI.repository.FacultyRepository;
import com.example.createAPI.repository.StudentRepository;
import com.example.createAPI.service.FacultyService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    StudentRepository studentRepository;

    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    FacultyService facultyService;

    @Test
    void getById() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1")  //создаём запрос
                        .accept(MediaType.APPLICATION_JSON)  //запрос принимает JSON
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$.name").value("math"));
    }

    @Test
    void create() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty/")
                        .content(objectMapper.writeValueAsString(faculty))  //преобразование объекта в JSON
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$.name").value("math"));
    }
    @Test
    void update() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty/1")
                        .content(objectMapper.writeValueAsString(faculty))  //преобразование объекта в JSON
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$.name").value("math"));
    }

    @Test
    void delete() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1")
                        .content(objectMapper.writeValueAsString(faculty))  //преобразование объекта в JSON
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    void filteredByColor() throws Exception {
        when(facultyRepository.findAllByColor("red"))
                .thenReturn(Arrays.asList(
                        new Faculty(1L, "math", "red"),
                        new Faculty(2L, "fiz", "blue")));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/filtered?color=red")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     //проверка статус 200
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].color").value("red"))
                .andExpect(jsonPath("$[1].color").value("blue"));

    }
}
