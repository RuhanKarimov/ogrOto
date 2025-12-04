package org.example.ogroto.service;

import org.example.ogroto.entity.Student;
import org.example.ogroto.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public Student create(Student s) {
        return repo.save(s);
    }

    public Optional<Student> findById(Long id) {
        return repo.findById(id);
    }

    public List<Student> findAll() {
        return repo.findAll();
    }

    public Student update(Student s) {
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

