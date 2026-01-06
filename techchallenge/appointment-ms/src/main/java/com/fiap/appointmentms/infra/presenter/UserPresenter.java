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
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .login(userEntity.getLogin())
                .password(userEntity.getPassword())
                .type(userEntity.getUserType())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
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
        return UserMessage.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .type(user.type())
                .build();
    }

    public static User toDomain(UserMessage doctor) {
        return User.builder()
                .id(doctor.id())
                .name(doctor.name())
                .email(doctor.email())
                .type(doctor.type())
                .build();
    }

    public static User toDomain(Long idUser) {
        return User.builder()
                .id(idUser)
                .build();
    }

    public static User toDomain(String username) {
        return User.builder()
                .login(username)
                .build();
    }
}
