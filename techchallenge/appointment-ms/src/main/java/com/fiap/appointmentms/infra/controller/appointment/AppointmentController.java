package com.fiap.appointmentms.infra.controller.appointment;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.core.usecase.appointment.RegisterAppointmentUsecase;
import com.fiap.appointmentms.core.usecase.appointment.UpdateAppointmentUsecase;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCreateRequest;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentUpdateRequest;
import com.fiap.appointmentms.infra.presenter.AppointmentPresenter;
import com.fiap.appointmentms.infra.presenter.AppointmentUpdatePresenter;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/appointment")
@PreAuthorize("hasAuthority('DOCTOR') or hasAuthority('NURSE')")
public class AppointmentController {

    private final RegisterAppointmentUsecase bookAppointmentUsecase;
    private final UpdateAppointmentUsecase updateAppointmentUsecase;

    public AppointmentController(RegisterAppointmentUsecase bookAppointmentUsecase, UpdateAppointmentUsecase updateAppointmentUsecase) {
        this.bookAppointmentUsecase = bookAppointmentUsecase;
        this.updateAppointmentUsecase = updateAppointmentUsecase;
    }

    @PostMapping
    public void create(@RequestBody @Valid AppointmentCreateRequest request, Principal principal){
        Appointment appointmentToCreate = AppointmentPresenter.toDomain(request, principal);
        bookAppointmentUsecase.execute(appointmentToCreate);
    }

    @PatchMapping("/update")
    public void update(@RequestBody @Valid AppointmentUpdateRequest request){
        AppointmentUpdate updateAppointment = AppointmentUpdatePresenter.toUpdateDomain(request);
        updateAppointmentUsecase.execute(updateAppointment);
    }

    @PatchMapping("/correction")
    public void correction(){

    }

    @PatchMapping("/cancel")
    public void cancel(){

    }


    @PatchMapping("/complete")
    public void complete(){

    }
}
