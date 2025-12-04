package org.example.ogroto.controller;

import org.example.ogroto.entity.Course;
import org.example.ogroto.entity.Enrollment;
import org.example.ogroto.entity.Student;
import org.example.ogroto.service.CourseService;
import org.example.ogroto.service.EnrollmentService;
import org.example.ogroto.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public StudentController(EnrollmentService enrollmentService,
                             StudentService studentService,
                             CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Enrollment> enroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        Student s = studentService.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        Course c = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        Enrollment e = enrollmentService.enroll(s, c);
        return ResponseEntity.ok(e);
    }

    @PostMapping("/{studentId}/drop/{courseId}")
    public ResponseEntity<Void> drop(@PathVariable Long studentId, @PathVariable Long courseId) {
        Student s = studentService.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        Course c = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        enrollmentService.drop(s, c);
        return ResponseEntity.ok().build();
    }
}

