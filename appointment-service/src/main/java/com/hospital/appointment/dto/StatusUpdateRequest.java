package com.hospital.appointment.dto;

import com.hospital.appointment.model.AppointmentStatus;

public class StatusUpdateRequest {

    private AppointmentStatus status;

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}

