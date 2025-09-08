package br.com.wisebuy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wisebuy.dto.SignInDTO;
import br.com.wisebuy.dto.SignUpDTO;
import br.com.wisebuy.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignUpDTO dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody SignInDTO dto) {
        return userService.login(dto);
    }

}
