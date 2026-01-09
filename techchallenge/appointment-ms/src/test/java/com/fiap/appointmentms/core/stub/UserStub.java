package com.fiap.appointmentms.core.stub;

import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.UserEntity;
import com.fiap.core.enums.UserTypeEnum;

public class UserStub {

    public static User createDoctor() {
        return User.builder()
                .id(1L)
                .login("doctor")
                .password("pass123")
                .type(UserTypeEnum.DOCTOR)
                .build();
    }

    public static User createNurse() {
        return User.builder()
                .id(2L)
                .login("nurse")
                .type(UserTypeEnum.NURSE)
                .build();
    }

    public static User createPatient() {
        return User.builder()
                .id(3L)
                .type(UserTypeEnum.PATIENT)
                .build();
    }


    public static UserEntity createEntityDoctor() {
        return UserEntity.builder()
                .id(1L)
                .login("doctor")
                .password("pass123")
                .userType(UserTypeEnum.DOCTOR)
                .build();
    }

    public static UserEntity createEntityNurse() {
        return UserEntity.builder()
                .id(2L)
                .login("nurse")
                .userType(UserTypeEnum.NURSE)
                .build();
    }

    public static UserEntity createEntityPatient() {
        return UserEntity.builder()
                .id(3L)
                .userType(UserTypeEnum.PATIENT)
                .build();
    }
}
