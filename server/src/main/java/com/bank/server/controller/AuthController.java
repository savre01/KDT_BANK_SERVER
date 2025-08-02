// controller/AuthController.java
package com.bank.server.controller;
import org.springframework.http.ResponseEntity;

import com.bank.server.dto.user.RegisterRequest;

import com.bank.server.dto.user.LoginRequest;
import com.bank.server.dto.user.LoginResponse;
import com.bank.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    String result = authService.register(request);
    return ResponseEntity.ok(result);
    }
}
