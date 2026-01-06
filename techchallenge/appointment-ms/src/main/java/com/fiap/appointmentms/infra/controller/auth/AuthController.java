package com.fiap.appointmentms.infra.controller.auth;

import com.fiap.appointmentms.core.usecase.auth.LoginUsecase;
import com.fiap.appointmentms.infra.controller.auth.request.LoginRequest;
import com.fiap.appointmentms.infra.controller.auth.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final LoginUsecase loginUsecase;

    public AuthController(LoginUsecase loginUsecase) {
        this.loginUsecase = loginUsecase;
    }

    @PostMapping
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        String token = loginUsecase.execute(request.login(), request.password());
        return new LoginResponse(token);
    }

    @PostMapping("/test")
    public String testPost(@RequestBody String body) {
        return "POST working with body: " + body;
    }

    @GetMapping("/test-db")
    public String testDb() {
        try {
            var user = loginUsecase.testDbConnection();
            return "Database connection working. Found user: " + user;
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Auth service is working!";
    }
}
