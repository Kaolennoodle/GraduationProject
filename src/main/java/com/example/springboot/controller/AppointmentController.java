package com.example.springboot.controller;

import com.example.springboot.entity.Appointment;
import com.example.springboot.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 新建预约
     *
     * @param uid
     * @param cid
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/new")
    public boolean makeANew(@RequestParam Integer uid,
                            @RequestParam Integer cid,
                            @RequestParam String date,
                            @RequestParam String startTime,
                            @RequestParam String endTime) throws ParseException {
        Appointment appointment = new Appointment();
        appointment.setUId(uid);
        appointment.setCId(cid);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date d = format.parse(date.replace("Z", " UTC"));
        System.out.println(df.format(d));
        startTime = df.format(d).toString() + " " + startTime;
        endTime = df.format(d).toString() + " " + endTime;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        System.out.println("An Output from AppointmentController: date = " + df.format(d) + ", startTime = " + startTime + ", endTime = " + endTime + ".");
        appointment.setAStartTime(df.parse(startTime));
        appointment.setAEndTime(df.parse(endTime));
        System.out.println("And the Appointment = " + appointment);

        return appointmentService.makeNew(appointment);
    }
}
