package com.example.springboot.controller;

import com.example.springboot.entity.Appointment;
import com.example.springboot.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 新建预约
     * @param appointment
     * @return
     */
    @PostMapping("/new")
    public boolean makeANew(@RequestBody Appointment appointment) {
        return appointmentService.makeNew(appointment);
    }
}
