package com.fiap.appointmentms.infra.gateway.spring.data.entity;

import com.fiap.core.enums.AppointmentEventEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idAppointment;

    private LocalDateTime dateTimeOfChange;

    @Enumerated(EnumType.STRING)
    private AppointmentEventEnum event;

    @Column(columnDefinition = "TEXT")
    private String detail;
}
