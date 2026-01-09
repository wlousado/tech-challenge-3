package com.fiap.appointmentms.infra.gateway.spring.data.entity;

import com.fiap.core.enums.AppointmentEventEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_timeline")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTimelineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Long idAppointment;
    Long patient;
    Long doctor;
    Long registeredBy;
    String observation;
    String cancellationReason;
    String correctedObservation;
    String justification;
    String cid;
    LocalDateTime dateTimeOfAppointment;

    @Enumerated(EnumType.STRING)
    AppointmentEventEnum event;

    LocalDateTime updatedAt;
}
