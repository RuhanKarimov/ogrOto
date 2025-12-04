package org.example.ogroto.service;

import org.example.ogroto.entity.Course;
import org.example.ogroto.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository repo;

    public CourseService(CourseRepository repo) {
        this.repo = repo;
    }

    public Course create(Course c) {
        return repo.save(c);
    }

    public Optional<Course> findById(Long id) {
        return repo.findById(id);
    }

    public List<Course> findAll() {
        return repo.findAll();
    }

    public Course update(Course c) {
        return repo.save(c);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

