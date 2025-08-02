// service/AuthService.java
package com.bank.server.service;

import com.bank.server.dto.user.RegisterRequest;
import com.bank.server.dto.user.LoginRequest;
import com.bank.server.dto.user.LoginResponse;

import com.bank.server.model.User;

import com.bank.server.repository.UserRepository;

import com.bank.server.security.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

   public LoginResponse login(LoginRequest request) {
        System.out.println("[로그인 시도] 아이디: " + request.getUserId());
        User user = userRepository.findByUserId(request.getUserId())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.isAdmin());
        
        return new LoginResponse(
                token,
                user.getUserIndex(),
                user.getUserName(),
                user.isAdmin()
        );
    }

    public String register(RegisterRequest request) {
    if (userRepository.findByUserId(request.getUserId()).isPresent()) {
        throw new RuntimeException("이미 존재하는 사용자 ID입니다.");
    }

    User user = new User();
    user.setUserId(request.getUserId());
    user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
    user.setUserName(request.getUserName());
    user.setUserPhone(request.getUserPhone());
    user.setUserBirth(request.getUserBirth());
    user.setDepartment(request.getDepartment());
    user.setPosition(request.getPosition());

    user.setAdmin(false); // ✅ 관리자 권한 false 고정

    userRepository.save(user);
    return "회원가입이 완료되었습니다.";
    }
}
