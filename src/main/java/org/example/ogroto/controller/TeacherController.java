package org.example.ogroto.controller;

import org.example.ogroto.entity.Course;
import org.example.ogroto.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final CourseService courseService;

    public TeacherController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course saved = courseService.create(course);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> listCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course c) {
        Course existing = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        existing.setTitle(c.getTitle());
        existing.setCode(c.getCode());
        existing.setTeacher(c.getTeacher());
        Course updated = courseService.update(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }
}

