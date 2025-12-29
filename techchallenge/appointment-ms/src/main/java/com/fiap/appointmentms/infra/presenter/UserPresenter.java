package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPresenter {

    private UserPresenter(){}

    public static User toDomain(UserDetails userDetails) {
        return new User(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority()
        );
    }

    public static User mapEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getUserType(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }

    public static UserEntity toEntity(User doc) {
        return new UserEntity(doc);
    }
}
