package org.example.ogroto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ogroto.entity.Course;
import org.example.ogroto.entity.Teacher;
import org.example.ogroto.entity.User;
import org.example.ogroto.repository.CourseRepository;
import org.example.ogroto.repository.TeacherRepository;
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
public class TeacherControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepo;
    @Autowired TeacherRepository teacherRepo;
    @Autowired CourseRepository courseRepo;
    @Autowired PasswordEncoder encoder;

    private String token;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setup() throws Exception {
        userRepo.deleteAll();
        teacherRepo.deleteAll();
        courseRepo.deleteAll();

        User u = new User();
        u.setUsername("hoca1");
        u.setPassword(encoder.encode("pwdh"));
        u.setRole("HOCA");
        u = userRepo.save(u);

        teacher = new Teacher();
        teacher.setName("Hoca Bir");
        teacher.setUser(u);
        teacher = teacherRepo.save(teacher);

        // login
        String loginJson = "{\"username\":\"hoca1\",\"password\":\"pwdh\"}";
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
    void createUpdateDeleteCourse_flow() throws Exception {
        // create course as teacher
        Course c = new Course();
        c.setCode("T200");
        c.setTitle("Test Course");
        c.setTeacher(teacher);
        String createJson = objectMapper.writeValueAsString(c);

        String createResp = mvc.perform(post("/api/teacher/courses")
                        .header("X-AUTH-TOKEN", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Course created = objectMapper.readValue(createResp, Course.class);
        assertThat(created.getId()).isNotNull();

        // update
        created.setTitle("Updated Title");
        String updateJson = objectMapper.writeValueAsString(created);

        mvc.perform(put("/api/teacher/courses/" + created.getId())
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk());

        // delete
        mvc.perform(delete("/api/teacher/courses/" + created.getId())
                .header("X-AUTH-TOKEN", token))
                .andExpect(status().isOk());
    }
}

