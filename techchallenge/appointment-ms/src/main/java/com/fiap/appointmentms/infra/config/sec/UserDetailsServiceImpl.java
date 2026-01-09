package com.fiap.appointmentms.infra.config.sec;

import com.fiap.appointmentms.infra.gateway.spring.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuarioOp = userRepository.findByLogin(username);

        if(usuarioOp.isPresent()) {
            var usuario = usuarioOp.get();
            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(usuario.getUserType().name())
            );
            return new UserSpringSec(username, usuario.getPassword(), authorities);
        }

        log.warn("Usuário não encontrado com login informado: {}", username);
        throw new UsernameNotFoundException("Usuário não encontrado com login informado: " + username);
    }

}
