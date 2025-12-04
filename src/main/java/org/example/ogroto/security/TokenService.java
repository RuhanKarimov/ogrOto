package org.example.ogroto.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private final Map<String, String> tokenToUsername = new ConcurrentHashMap<>();

    public String createTokenForUser(String username) {
        String token = UUID.randomUUID().toString();
        tokenToUsername.put(token, username);
        return token;
    }

    public Optional<String> getUsernameForToken(String token) {
        return Optional.ofNullable(tokenToUsername.get(token));
    }

    public void revokeToken(String token) {
        tokenToUsername.remove(token);
    }
}

