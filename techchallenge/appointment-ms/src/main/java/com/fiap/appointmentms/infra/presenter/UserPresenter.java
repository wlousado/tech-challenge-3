package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.UserEntity;
import com.fiap.core.message.UserMessage;
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

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .login(user.login())
                .password(user.password())
                .userType(user.type())
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .build();
    }

    public static UserMessage toMessage(User user) {
        return new UserMessage(user.id(), user.name(), user.email(), user.type());
    }

    public static User toDomain(UserMessage doctor) {
        return User.builder()
                .id(doctor.id())
                .name(doctor.name())
                .email(doctor.email())
                .type(doctor.type())
                .build();
    }
}
