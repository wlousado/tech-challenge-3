package com.fiap.appointmentms.infra.controller.appointment;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.usecase.appointment.BookAppointmentUsecase;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCreateRequest;
import com.fiap.appointmentms.infra.controller.appointment.response.AppointmentResponse;
import com.fiap.appointmentms.infra.presenter.AppointmentPresenter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private final BookAppointmentUsecase bookAppointmentUsecase;

    public AppointmentController(BookAppointmentUsecase bookAppointmentUsecase) {
        this.bookAppointmentUsecase = bookAppointmentUsecase;
    }

    @PostMapping
    public void create(@RequestBody @Valid AppointmentCreateRequest request){
        Appointment appointmentToCreate = AppointmentPresenter.toDomain(request);
        bookAppointmentUsecase.execute(appointmentToCreate);
    }
}
