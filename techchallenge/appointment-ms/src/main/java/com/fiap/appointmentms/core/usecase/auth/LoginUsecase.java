package com.fiap.appointmentms.core.usecase.auth;

import com.fiap.appointmentms.core.gateway.EncryptGateway;
import com.fiap.appointmentms.core.gateway.TokenGateway;
import com.fiap.appointmentms.core.gateway.UserGateway;
import com.fiap.core.exception.AuthLoginError;
import org.springframework.stereotype.Service;

@Service
public class LoginUsecase {

    private final EncryptGateway encryptGateway;
    private final TokenGateway tokenGateway;
    private final UserGateway userGateway;

    public LoginUsecase(EncryptGateway encryptGateway, TokenGateway tokenGateway, UserGateway userGateway) {
        this.encryptGateway = encryptGateway;
        this.tokenGateway = tokenGateway;
        this.userGateway = userGateway;
    }

    public String execute(String login, String password){
        var userByLogin = userGateway.findByLogin(login);
        if(userByLogin.isPresent()){
            var usuarioDomain = userByLogin.get();
            if(!encryptGateway.validate(password, usuarioDomain.password())){
                throw new AuthLoginError("Senha invalida");
            }
            return tokenGateway.generate(usuarioDomain);
        }
        throw new AuthLoginError("Login {} falhou".replace("{}", login));
    }
}
