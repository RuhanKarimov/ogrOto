package org.example.ogroto.service;

import org.example.ogroto.repository.UserRepository;
import org.example.ogroto.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authManager, TokenService tokenService, UserRepository userRepository) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public String login(String username, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenService.createTokenForUser(username);
    }

    public void logout(String token) {
        tokenService.revokeToken(token);
    }
}

