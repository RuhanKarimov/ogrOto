package org.example.ogroto.controller;

import org.example.ogroto.entity.Course;
import org.example.ogroto.entity.Student;
import org.example.ogroto.entity.Teacher;
import org.example.ogroto.entity.User;
import org.example.ogroto.repository.TeacherRepository;
import org.example.ogroto.service.CourseService;
import org.example.ogroto.service.StudentService;
import org.example.ogroto.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TeacherRepository teacherRepository;

    public AdminController(UserService userService,
                           StudentService studentService,
                           CourseService courseService,
                           TeacherRepository teacherRepository) {
        this.userService = userService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.teacherRepository = teacherRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userService.createUser(user);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody Student s) {
        Student saved = studentService.create(s);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/teachers")
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher t) {
        Teacher saved = teacherRepository.save(t);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course c) {
        Course saved = courseService.create(c);
        return ResponseEntity.ok(saved);
    }
}

