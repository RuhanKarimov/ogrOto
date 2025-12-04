package org.example.ogroto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ogroto.entity.Course;
import org.example.ogroto.entity.Student;
import org.example.ogroto.entity.Teacher;
import org.example.ogroto.entity.User;
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

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepo;
    @Autowired PasswordEncoder encoder;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        userRepo.deleteAll();
        User u = new User();
        u.setUsername("admin1");
        u.setPassword(encoder.encode("adm1"));
        u.setRole("ADMIN");
        userRepo.save(u);

        String loginJson = "{\"username\":\"admin1\",\"password\":\"adm1\"}";
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
    void createUserStudentTeacherCourse_flow() throws Exception {
        // create user
        User u = new User();
        u.setUsername("newu");
        u.setPassword("pass");
        u.setRole("OGRENCI");
        String userJson = objectMapper.writeValueAsString(u);

        mvc.perform(post("/api/admin/users")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk());

        // create student
        Student s = new Student();
        s.setName("Yeni Ogrenci");
        String studentJson = objectMapper.writeValueAsString(s);

        mvc.perform(post("/api/admin/students")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk());

        // create teacher
        Teacher t = new Teacher();
        t.setName("Yeni Hoca");
        String teacherJson = objectMapper.writeValueAsString(t);

        mvc.perform(post("/api/admin/teachers")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(teacherJson))
                .andExpect(status().isOk());

        // create course
        Course c = new Course();
        c.setCode("AC1");
        c.setTitle("Admin Course");
        String courseJson = objectMapper.writeValueAsString(c);

        mvc.perform(post("/api/admin/courses")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseJson))
                .andExpect(status().isOk());
    }
}

