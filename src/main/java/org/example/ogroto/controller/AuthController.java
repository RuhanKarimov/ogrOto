package org.example.ogroto.controller;

import org.example.ogroto.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final String tokenHeaderName;

    public AuthController(AuthService authService, @Value("${security.token.header:X-AUTH-TOKEN}") String tokenHeaderName) {
        this.authService = authService;
        this.tokenHeaderName = tokenHeaderName;
    }

    public static record LoginRequest(String username, String password) {}
    public static record LoginResponse(String token) {}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req.username(), req.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = "${security.token.header:X-AUTH-TOKEN}", required = false) String token) {
        if (token != null && !token.isBlank()) {
            authService.logout(token);
        }
        return ResponseEntity.ok().build();
    }
}

