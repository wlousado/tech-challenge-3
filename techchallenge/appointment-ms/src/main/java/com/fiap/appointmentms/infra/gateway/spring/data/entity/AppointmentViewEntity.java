package com.fiap.appointmentms.infra.gateway.spring.data.entity;


import com.fiap.core.enums.AppointmentEventEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_view")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentViewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idAppointment;

    @Enumerated(EnumType.STRING)
    private AppointmentEventEnum event;
    private LocalDateTime updatedAt;
}
