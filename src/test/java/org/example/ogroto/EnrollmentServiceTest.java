package org.example.ogroto;

import org.example.ogroto.entity.Course;
import org.example.ogroto.entity.Enrollment;
import org.example.ogroto.entity.Student;
import org.example.ogroto.repository.CourseRepository;
import org.example.ogroto.repository.EnrollmentRepository;
import org.example.ogroto.repository.StudentRepository;
import org.example.ogroto.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EnrollmentServiceTest {

    @Autowired EnrollmentService enrollmentService;
    @Autowired StudentRepository studentRepo;
    @Autowired CourseRepository courseRepo;
    @Autowired EnrollmentRepository enrollmentRepo;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        enrollmentRepo.deleteAll();
        studentRepo.deleteAll();
        courseRepo.deleteAll();

        student = new Student();
        student.setName("Test Student");
        student = studentRepo.save(student);

        course = new Course();
        course.setCode("C101");
        course.setTitle("Test Course");
        course = courseRepo.save(course);
    }

    @Test
    void enroll_createsEnrollment_ifNotExists() {
        Enrollment e = enrollmentService.enroll(student, course);
        assertThat(e).isNotNull();
        assertThat(enrollmentRepo.findById(e.getId())).isPresent();
    }

    @Test
    void drop_removesEnrollment_ifExists() {
        Enrollment e = enrollmentService.enroll(student, course);
        assertThat(enrollmentRepo.findById(e.getId())).isPresent();

        enrollmentService.drop(student, course);
        assertThat(enrollmentRepo.findById(e.getId())).isNotPresent();
    }
}

