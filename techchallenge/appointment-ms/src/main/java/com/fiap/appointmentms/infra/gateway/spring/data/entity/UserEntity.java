package com.fiap.appointmentms.infra.gateway.spring.data.entity;

import com.fiap.appointmentms.core.domain.User;
import com.fiap.core.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_appointment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private UserTypeEnum userType;

    public UserEntity(User doc) {
        this.name = doc.name();
        this.email = doc.email();
        this.login = doc.login();
        this.password = doc.password();
        this.userType = doc.type();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }
}
