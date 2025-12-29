package com.fiap.appointmentms.infra.controller.auth;

import com.fiap.appointmentms.core.usecase.auth.LoginUsecase;
import com.fiap.appointmentms.infra.controller.auth.request.LoginRequest;
import com.fiap.appointmentms.infra.controller.auth.response.LoginResponse;
import jakarta.validation.Valid;
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
}
