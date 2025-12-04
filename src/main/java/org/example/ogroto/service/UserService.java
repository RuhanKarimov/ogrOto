package org.example.ogroto.service;

import org.example.ogroto.entity.User;
import org.example.ogroto.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User createUser(User u) {
        u.setPassword(encoder.encode(u.getPassword()));
        return repo.save(u);
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User update(User u) {
        return repo.save(u);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

