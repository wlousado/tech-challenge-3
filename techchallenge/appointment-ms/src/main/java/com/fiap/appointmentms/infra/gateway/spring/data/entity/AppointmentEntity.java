package com.fiap.appointmentms.infra.gateway.spring.data.entity;


import com.fiap.core.enums.AppointmentEventEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private UserEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_by_id")
    private UserEntity registeredBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private UserEntity patient;

    private String observation;

    @Enumerated(EnumType.STRING)
    private AppointmentEventEnum event;

    private LocalDateTime dateOfAppointment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
