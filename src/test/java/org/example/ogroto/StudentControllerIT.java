package org.example.ogroto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ogroto.entity.Course;
import org.example.ogroto.entity.Student;
import org.example.ogroto.entity.User;
import org.example.ogroto.repository.CourseRepository;
import org.example.ogroto.repository.StudentRepository;
import org.example.ogroto.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepo;
    @Autowired StudentRepository studentRepo;
    @Autowired CourseRepository courseRepo;
    @Autowired PasswordEncoder encoder;

    private String token;
    private Student student;
    private Course course;

    @BeforeEach
    void setup() throws Exception {
        userRepo.deleteAll();
        studentRepo.deleteAll();
        courseRepo.deleteAll();

        // create user and student
        User u = new User();
        u.setUsername("stud1");
        u.setPassword(encoder.encode("pwd"));
        u.setRole("OGRENCI");
        u = userRepo.save(u);

        student = new Student();
        student.setName("Stud One");
        student.setUser(u);
        student = studentRepo.save(student);

        course = new Course();
        course.setCode("M101");
        course.setTitle("Math");
        course = courseRepo.save(course);

        // login to get token
        String loginJson = "{\"username\":\"stud1\",\"password\":\"pwd\"}";
        String resp = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode node = objectMapper.readTree(resp);
        token = node.get("token").asText();
        assertThat(token).isNotBlank();
    }

    @Test
    void enrollAndDrop_flow() throws Exception {
        // enroll
        mvc.perform(post("/api/student/" + student.getId() + "/enroll/" + course.getId())
                .header("X-AUTH-TOKEN", token))
                .andExpect(status().isOk());

        // drop
        mvc.perform(post("/api/student/" + student.getId() + "/drop/" + course.getId())
                .header("X-AUTH-TOKEN", token))
                .andExpect(status().isOk());
    }
}

