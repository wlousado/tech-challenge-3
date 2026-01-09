package com.fiap.appointmentms.infra.controller.appointment;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentCancelled;
import com.fiap.appointmentms.core.domain.AppointmentCorrection;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.core.usecase.appointment.*;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCancelRequest;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCorrectionRequest;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCreateRequest;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentUpdateRequest;
import com.fiap.appointmentms.infra.presenter.AppointmentCancelledPresenter;
import com.fiap.appointmentms.infra.presenter.AppointmentCorrectionPresenter;
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
    private final CorrectionAppointmentUsecase correctionAppointmentUsecase;
    private final CancelledAppointmentUsecase cancelledAppointmentUsecase;
    private final CompletedAppointmentUsecase completedAppointmentUsecase;

    public AppointmentController(RegisterAppointmentUsecase bookAppointmentUsecase, UpdateAppointmentUsecase updateAppointmentUsecase, CorrectionAppointmentUsecase correctionAppointmentUsecase, CancelledAppointmentUsecase cancelledAppointmentUsecase, CompletedAppointmentUsecase completedAppointmentUsecase) {
        this.bookAppointmentUsecase = bookAppointmentUsecase;
        this.updateAppointmentUsecase = updateAppointmentUsecase;
        this.correctionAppointmentUsecase = correctionAppointmentUsecase;
        this.cancelledAppointmentUsecase = cancelledAppointmentUsecase;
        this.completedAppointmentUsecase = completedAppointmentUsecase;
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
    public void correction(@RequestBody @Valid AppointmentCorrectionRequest request){
        AppointmentCorrection correction = AppointmentCorrectionPresenter.toDomain(request);
        correctionAppointmentUsecase.execute(correction);
    }

    @PatchMapping("/cancel")
    public void cancel(@RequestBody @Valid AppointmentCancelRequest request){
        AppointmentCancelled cancelled = AppointmentCancelledPresenter.toDomain(request);
        cancelledAppointmentUsecase.execute(cancelled);
    }

    @PatchMapping("/complete/{idAppointment}")
    public void complete(@PathVariable Long idAppointment){
        completedAppointmentUsecase.execute(idAppointment);
    }
}
