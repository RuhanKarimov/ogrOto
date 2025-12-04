package org.example.ogroto.service;

import org.example.ogroto.entity.Course;
import org.example.ogroto.entity.Enrollment;
import org.example.ogroto.entity.Student;
import org.example.ogroto.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    private final EnrollmentRepository repo;

    public EnrollmentService(EnrollmentRepository repo) {
        this.repo = repo;
    }

    public Enrollment enroll(Student s, Course c) {
        Optional<Enrollment> existing = repo.findByStudentIdAndCourseId(s.getId(), c.getId());
        if (existing.isPresent()) return existing.get();
        Enrollment e = new Enrollment();
        e.setStudent(s);
        e.setCourse(c);
        return repo.save(e);
    }

    public void drop(Student s, Course c) {
        repo.findByStudentIdAndCourseId(s.getId(), c.getId())
                .ifPresent(repo::delete);
    }

    public List<Enrollment> findAll() {
        return repo.findAll();
    }

    public Optional<Enrollment> findById(Long id) {
        return repo.findById(id);
    }
}

