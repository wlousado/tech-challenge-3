package com.fiap.appointmentms.infra.gateway.spring.data.repository;

import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistoryEntity, Long> {
}
